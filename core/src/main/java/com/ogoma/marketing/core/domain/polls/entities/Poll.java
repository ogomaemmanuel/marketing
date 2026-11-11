package com.ogoma.marketing.core.domain.polls.entities;

import com.ogoma.marketing.core.domain.polls.exceptions.*;
import com.ogoma.marketing.core.domain.polls.valueobjects.ListStyle;
import com.ogoma.marketing.core.domain.polls.valueobjects.Orientation;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Table(name = "polls")
public class Poll extends AggregateRoot<PollID> {

    /**
     * Name of the poll ,e.g. Preferred Candidate Survey
     */

    private String name;
    /**
     * where one is redirected after voting
     */
    private String redirectURL;

    private PollType pollType;

    /**
     * the poll question, eg who is your preferred presidential candidate
     */
    private String question;
    private String questionDescription;

    /**
     * options available on the survey, eg Kibaki, Raila,Ngilu etc
     */

    @MappedCollection(idColumn = "poll_id")
    private Set<Option> options;
    /**
     * how the poll options will appear
     */
    private Orientation orientation;

    /**
     * used when orientation is vertical,defaults to NONE
     */
    private ListStyle listStyle;

    /**
     * whether result should be shown once one submits the feedback, for trivia questions,
     * this will show correct answer, for voting polls, this will show percentages
     */

    private boolean showResult;

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    private Instant createdAt;
    private String createdBy;
    private PollStatus status;

    private Poll() {
        super(null);
    }

    private Poll(
            String name,
            String question,
            String questionDescription,
            String redirectURL,
            Set<String> options,
            Orientation orientation,
            ListStyle listStyle,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            String createdBy,
            Instant createdAt

    ) {
        super(new PollID());
        this.name = name;
        this.redirectURL = redirectURL;
        this.question = question;
        this.questionDescription = questionDescription;
        this.options = optionsFromStringSet(options);
        this.listStyle = Objects.requireNonNullElse(listStyle, ListStyle.NONE);
        this.orientation = Objects.requireNonNullElse(orientation, Orientation.VERTICAL);
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.pollType = PollType.VOTE;
        this.showResult = false;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = PollStatus.DRAFT;
        validateState();
    }

    public static Poll createNew(
            String name,
            String question,
            String questionDescription,
            String redirectURL,
            Set<String> options,
            Orientation orientation,
            ListStyle listStyle,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            String createdBy,
            Clock clock
    ) {
        return new Poll(
                name,
                question,
                questionDescription,
                redirectURL, options,
                orientation,
                listStyle,
                startTime,
                endTime,
                createdBy,
                clock.instant()
        );
    }

    private Set<Option> optionsFromStringSet(Set<String> options) {
        return options.stream().map(Option::new).collect(Collectors.toSet());
    }

    public Set<Option> getOptions() {
        return Collections.unmodifiableSet(this.options);
    }

    public void validateOption(String feedback) {
        if (feedback == null || feedback.isBlank()) {
            throw new IllegalArgumentException("Feedback cannot be null or blank");
        }

        boolean isValid = options.stream()
                .anyMatch(option -> option.value().equals(feedback));

        if (!isValid) {
            throw new InvalidPollOptionException("Invalid poll option selected: " + feedback);
        }
    }

    public void close() {
        this.status = PollStatus.CLOSED;
    }

    public void publish() {
        if (this.status != PollStatus.DRAFT) {
            throw new PollPublicationException(
                    "Only draft polls can be published"
            );
        }
    }

    public boolean isAcceptingVotes(Clock clock) {
        if (status != PollStatus.PUBLISHED) {
            return false;
        }
        Instant now = clock.instant();
        return !now.isBefore(startTime.toInstant())
                && now.isBefore(endTime.toInstant());
    }

    public void validateCanAcceptVotes(Clock clock) {
        Instant now = clock.instant();

        if (this.status != PollStatus.PUBLISHED) { // Assuming PUBLISHED or ACTIVE is your running status
            throw new PollNotPublishedException("Poll is not currently active for voting");
        }
        if (now.isBefore(this.startTime.toInstant())) {
            throw new PollNotStartedException("Poll has not started yet");
        }
        if (now.isAfter(this.endTime.toInstant())) {
            throw new PollClosedException("Poll has already ended");
        }
    }

    void validateState() {
        Assert.hasText(name, "Name is required");
        Assert.hasText(question, "Question is required");
        Assert.notEmpty(options, "Provide an option");
        Assert.noNullElements(options, "Option cannot contain null");
        Assert.isTrue(options.size() > 1, "A poll must contain at least two options");
        Assert.notNull(startTime, "Start time cannot be null");
        Assert.notNull(endTime, "End time cannot be null");
        Assert.isTrue(endTime.isAfter(startTime), "End time cannot be null");
    }
}

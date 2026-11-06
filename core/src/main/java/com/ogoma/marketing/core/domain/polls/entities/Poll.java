package com.ogoma.marketing.core.domain.polls.entities;

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

    private Instant createdAt;
    private String createdBy;

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
            String createdBy,
            Instant createdAt
    ) {
        Assert.hasText(name, "Name is required");
        Assert.hasText(question, "Question is required");
        Assert.notEmpty(options, "Provide an option");
        Assert.noNullElements(options, "Option cannot contain null");
        Assert.isTrue(options.size() > 1, "A poll must contain at least two options");
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
    }

    public static Poll createNew(
            String name,
            String question,
            String questionDescription,
            String redirectURL,
            Set<String> options,
            Orientation orientation,
            ListStyle listStyle,
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

}

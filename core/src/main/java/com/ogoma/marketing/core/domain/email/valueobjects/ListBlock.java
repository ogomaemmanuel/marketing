package com.ogoma.marketing.core.domain.email.valueobjects;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class ListBlock extends BaseEmailBlock {


    @Pattern(regexp = "^(ordered|unordered|checked|unchecked)$", message = "Invalid list style")
    private String style;
    private List<String> items;

    private List<Boolean> checkedItems;

    public ListBlock() {
        this.items = new ArrayList<>();
    }


    @Override
    public String renderHtml() {
        var block = this;
        if ("ordered".equals(this.getStyle())) {
            StringBuilder listItems = new StringBuilder();
            for (String item : getItems()) {
                listItems.append("""
                        <li>%s</li>
                        """.formatted(item));
            }
            return """
                    <ol style="%s margin: 0; padding-left: 20px;">
                        %s
                    </ol>
                    """.formatted(block.baseStyle(), listItems.toString());
        }

        if ("checked".equals(style) || "unchecked".equals(getStyle())) {
            StringBuilder listItems = new StringBuilder();
            for (int i = 0; i < getItems().size(); i++) {
                String item = getItems().get(i);
                boolean isChecked = "checked".equals(getStyle())
                        && getCheckedItems() != null
                        && i < getCheckedItems().size()
                        && Boolean.TRUE.equals(getCheckedItems().get(i));

                String checkSymbol = isChecked ? "✓" : "☐";
                String textStyle = isChecked ? "text-decoration: line-through; color: #6b7280;" : "";
                String checkColor = isChecked ? "#10b981" : "#9ca3af";

                listItems.append("""
                        <div style="display: flex; align-items: flex-start; gap: 8px; margin-bottom: 8px;">
                            <span style="color: %s; font-weight: bold;">%s</span>
                            <span style="%s">%s</span>
                        </div>
                        """.formatted(checkColor, checkSymbol, textStyle, item));
            }

            return """
                    <div style="%s margin: 0;">
                        %s
                    </div>
                    """.formatted(baseStyle(), listItems.toString());
        }

        // ----- Unordered list (default) -----
        StringBuilder listItems = new StringBuilder();
        for (String item : getItems()) {
            listItems.append("""
                    <li>%s</li>
                    """.formatted(item));
        }

        return """
                <ul style="%s margin: 0; padding-left: 20px;">
                    %s
                </ul>
                """.formatted(baseStyle(), listItems.toString());
    }
}

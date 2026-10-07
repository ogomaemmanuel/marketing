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
        if (this.getItems() == null || this.getItems().isEmpty()) {
            return "";
        }

        TextAlignment alignment = (this.getAlign() != null ) ? this.getAlign() : TextAlignment.LEFT;

        if ("ordered".equals(this.getStyle())) {
            StringBuilder listItems = new StringBuilder();
            for (String item : getItems()) {
                listItems.append("""
                        <li style="margin-bottom: 4px;">%s</li>
                        """.formatted(item));
            }
            return """
                    <mj-text align="%s" style="%s">
                        <ol style="margin: 0; padding-left: 20px;">
                            %s
                        </ol>
                    </mj-text>
                    """.formatted(alignment.toCss(), baseStyle(), listItems.toString());
        }

        if ("checked".equals(this.getStyle()) || "unchecked".equals(this.getStyle())) {
            StringBuilder listItems = new StringBuilder();
            for (int i = 0; i < getItems().size(); i++) {
                String item = getItems().get(i);
                boolean isChecked = "checked".equals(this.getStyle())
                        && getCheckedItems() != null
                        && i < getCheckedItems().size()
                        && Boolean.TRUE.equals(getCheckedItems().get(i));

                String checkSymbol = isChecked ? "&#10003;" : "&#9633;";
                String textStyle = isChecked ? "text-decoration: line-through; color: #6b7280;" : "";
                String checkColor = isChecked ? "#10b981" : "#9ca3af";

                // Using standard HTML table layout inside mj-text for Outlook display compatibility instead of flexbox
                listItems.append("""
                        <tr>
                            <td style="vertical-align: top; width: 24px; color: %s; font-weight: bold; padding-bottom: 8px;">%s</td>
                            <td style="vertical-align: top; padding-bottom: 8px; %s">%s</td>
                        </tr>
                        """.formatted(checkColor, checkSymbol, textStyle, item));
            }

            return """
                    <mj-text align="%s" style="%s">
                        <table border="0" cellpadding="0" cellspacing="0" style="width: 100%%; border-collapse: collapse;">
                            <tbody>
                                %s
                            </tbody>
                        </table>
                    </mj-text>
                    """.formatted(alignment.toCss(), baseStyle(), listItems.toString());
        }

        // ----- Unordered list (default) -----
        StringBuilder listItems = new StringBuilder();
        for (String item : getItems()) {
            listItems.append("""
                    <li style="margin-bottom: 4px;">%s</li>
                    """.formatted(item));
        }

        return """
                <mj-section>
                <mj-column>
                <mj-text align="%s" style="%s">
                    <ul style="margin: 0; padding-left: 20px;">
                        %s
                    </ul>
                </mj-text>
                </mj-column>
                </mj-section>
                """.formatted(alignment.toCss(), baseStyle(), listItems.toString());
    }
}
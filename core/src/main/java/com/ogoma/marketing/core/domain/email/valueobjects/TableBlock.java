package com.ogoma.marketing.core.domain.email.valueobjects;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class TableBlock extends BaseEmailBlock {
    private int rows;
    private int columns;
    private boolean hasHeader;
    private List<List<String>> data;

    @Override
    protected String renderContent() {
        if (this.getData() == null || this.getData().isEmpty()) {
            return "";
        }

        StringBuilder headerHtml = new StringBuilder();
        StringBuilder bodyHtml = new StringBuilder();

        // ----- Header -----
        if (this.isHasHeader()) {
            List<String> headerRow = this.getData().getFirst();
            StringBuilder headerCells = new StringBuilder();
            for (String cell : headerRow) {
                headerCells.append("""
                        <th style="border: 1px solid %s; background-color: %s; color: %s; padding: 12px; text-align: left;">%s</th>
                        """.formatted(EmailTheme.BORDER_COLOR, EmailTheme.BODY_BACKGROUND, EmailTheme.HEADING_COLOR, cell));
            }
            headerHtml.append("""
                    <thead>
                        <tr>
                            %s
                        </tr>
                    </thead>
                    """.formatted(headerCells.toString()));
        }

        // ----- Body -----
        int startRow = this.isHasHeader() ? 1 : 0;
        for (int i = startRow; i < this.getData().size(); i++) {
            List<String> row = this.getData().get(i);
            StringBuilder rowCells = new StringBuilder();
            for (String cell : row) {
                rowCells.append("""
                        <td style="border: 1px solid %s; color: %s; padding: 12px;">%s</td>
                        """.formatted(EmailTheme.BORDER_COLOR, EmailTheme.TEXT_COLOR, cell));
            }
            bodyHtml.append("""
                    <tr>
                        %s
                    </tr>
                    """.formatted(rowCells.toString()));
        }

        // ----- Final MJML -----
        return """
                <mj-table font-family="%s" padding="0" width="100%%" style="border-collapse: collapse; width: 100%%; border: 1px solid %s;">
                    %s
                    <tbody>
                        %s
                    </tbody>
                </mj-table>
                """.formatted(
                EmailTheme.FONT_FAMILY,
                EmailTheme.BORDER_COLOR,
                headerHtml.toString(),
                bodyHtml.toString()
        );
    }
}
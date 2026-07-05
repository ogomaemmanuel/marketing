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
    public String renderHtml() {
        StringBuilder headerHtml = new StringBuilder();
        StringBuilder bodyHtml = new StringBuilder();
        if (this.isHasHeader() && this.getData() != null && !this.getData().isEmpty()) {
            List<String> headerRow = this.getData().getFirst();
            StringBuilder headerCells = new StringBuilder();
            for (String cell : headerRow) {
                headerCells.append("""
                        <th style="border: 1px solid #d1d5db; background-color: #f3f4f6; padding: 12px; text-align: left;">%s</th>
                        """.formatted(cell));
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
                        <td style="border: 1px solid #d1d5db; padding: 12px;">%s</td>
                        """.formatted(cell));
            }
            bodyHtml.append("""
                    <tr>
                        %s
                    </tr>
                    """.formatted(rowCells.toString()));
        }

        // ----- Final HTML -----
        return """
                <div style="%s">
                    <table style="border-collapse: collapse; width: 100%%; border: 1px solid #d1d5db;">
                        %s
                        <tbody>
                            %s
                        </tbody>
                    </table>
                </div>
                """.formatted(
                baseStyle(),
                headerHtml.toString(),
                bodyHtml.toString()
        );
    }
}

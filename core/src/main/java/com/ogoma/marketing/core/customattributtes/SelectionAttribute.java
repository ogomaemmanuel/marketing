package com.ogoma.marketing.core.customattributtes;

import java.util.List;

public record SelectionAttribute(List<String> options, boolean allowMultiple) {

}

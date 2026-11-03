package com.ogoma.marketing.core.domain.forms;


import com.ogoma.marketing.core.domain.forms.valueobjects.FormBlock;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "signup_forms")
public class SignupForm  extends AggregateRoot<SignupFormID> {
   private String name;
   List<FormBlock> blocks;
    private SignupForm() {
        super(null);
    }
}

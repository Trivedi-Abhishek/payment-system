package com.models;

import com.enums.FraudCheckEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResult {
    private FraudCheckEnum fraudCheck;
    private List<String> flagList;
}

A merchant with id:{merchantId} has been merked suspicious because of flags: {flags} check whether this merchant is fraud or valid.
Respond with ONLY this JSON structure, nothing else:
{
    "decision": "FRAUD" or "VALID",
    "confidence": number between 0.0 and 1.0,
    "reason": "one sentence explanation"
}
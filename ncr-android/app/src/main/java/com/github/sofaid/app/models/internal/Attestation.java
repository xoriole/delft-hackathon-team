package com.github.sofaid.app.models.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Attestation {
    String address;
    boolean attested;
}

package test.common.service;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
class FilterAttribute {

    @NonNull
    private String attributeName;

    private boolean unique = false;
}

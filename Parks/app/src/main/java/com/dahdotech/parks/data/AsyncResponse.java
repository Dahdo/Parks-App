package com.dahdotech.parks.data;

import com.dahdotech.parks.model.Park;

import java.util.List;

public interface AsyncResponse {
    void processPark(List<Park> parks);
}

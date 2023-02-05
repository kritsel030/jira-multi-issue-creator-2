package com.kramphub.jiramic.domain.jira;

import java.net.URI;
import java.util.List;

public class UserSearchResponse {

    // The URL of the page.
    private URI self;

    // If there is another page of results, the URL of the next page.
    private URI nextPage;

    // The maximum number of items that could be returned.

    // The index of the first item returned.
    private int startAt;

    //The number of items returned.
    private int total;

    // Whether this is the last page.
    private boolean isLast;

    private List<User> values;
}

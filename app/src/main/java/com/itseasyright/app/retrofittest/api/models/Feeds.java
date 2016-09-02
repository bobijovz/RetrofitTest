package com.itseasyright.app.retrofittest.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jovijovs on 02/09/2016.
 */
public class Feeds {
    private String timeline_url;
    private String user_url;
    @SerializedName("_links")
    private Links links;
    private Links user;

    public String getTimeline_url() {
        return timeline_url;
    }

    public String getUser_url() {
        return user_url;
    }

    public Links getLinks() {
        return links;
    }

    public Links getUser() {
        return user;
    }

    public class Links{
        private Timeline timeline;

        public Timeline getTimeline() {
            return timeline;
        }

        public class Timeline{
            private String href;
            private String type;

            public String getHref() {
                return href;
            }

            public String getType() {
                return type;
            }
        }
    }
}

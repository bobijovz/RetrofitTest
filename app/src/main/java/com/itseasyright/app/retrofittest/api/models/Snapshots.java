package com.itseasyright.app.retrofittest.api.models;


import java.util.ArrayList;

/**
 * Created by jovijovs on 20/09/2016.
 */
public class Snapshots {
    private ArrayList<Streams> streams;

    public ArrayList<Streams> getStreams() {
        return streams;
    }

    public class Streams{
        private String namespace;
        private String size;
        private String snapshot;
        private String framecount;
        private String created_at;

        public String getNamespace() {
            return namespace;
        }

        public String getSize() {
            return size;
        }

        public String getSnapshots() {
            return snapshot;
        }

        public String getFramecount() {
            return framecount;
        }

        public String getCreated_at() {
            return created_at;
        }
    }

}

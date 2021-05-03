package xyz.theclashfruit.kvantum;

import java.util.ArrayList;

public class TabItem {
    private String mUrl;

    public TabItem(String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    private static int lastContactId = 0;

    public static ArrayList<TabItem> createContactsList(int numContacts) {
        ArrayList<TabItem> contacts = new ArrayList<TabItem>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new TabItem("kvantum://home"));
        }

        return contacts;
    }
}

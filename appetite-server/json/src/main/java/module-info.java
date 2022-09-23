module appetite.json {
    exports json;

    requires transitive appetite.core;

    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;

}
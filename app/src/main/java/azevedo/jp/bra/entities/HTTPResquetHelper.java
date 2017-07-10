package azevedo.jp.bra.entities;

/**
 * Created by joaop on 07/07/2017.
 */

public class HTTPResquetHelper {
    private int id;
    private int limit;
    private int offset;
    private String filter;
    private String email;
    private String url;

    public HTTPResquetHelper(HTTPResquetHelperBuilder builder){
        this.id = builder.id;
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.filter = builder.filter;
        this.email = builder.email;
        this.url = builder.url;
    }

    public int getId() {
        return id;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public String getFilter() {
        return filter;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public static class HTTPResquetHelperBuilder{
        private int id;
        private int limit;
        private int offset;
        private String filter;
        private String email;
        private String url;

        public HTTPResquetHelperBuilder setId(int id){
            this.id = id;
            return this;
        }

        public HTTPResquetHelperBuilder setLimit(int limit){
            this.limit = limit;
            return this;
        }

        public HTTPResquetHelperBuilder setOffset(int offset){
            this.offset = offset;
            return this;
        }

        public HTTPResquetHelperBuilder setFilter(String filter){
            this.filter = filter;
            return this;
        }

        public HTTPResquetHelperBuilder setEmail(String email){
            this.email = email;
            return this;
        }

        public HTTPResquetHelperBuilder setUrl(String url){
            this.url = url;
            return this;
        }

        public HTTPResquetHelper build(){
            return new HTTPResquetHelper(this);
        }

    }

}

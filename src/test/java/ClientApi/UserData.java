package ClientApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
//@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserData {
 @JsonProperty("id")
 private Long id;
 @JsonProperty("post_id")
 private Long post_id;
 @JsonProperty("name")
 private String name;
 @JsonProperty("email")
 private String email;
 @JsonProperty("body")
 private String body;
}

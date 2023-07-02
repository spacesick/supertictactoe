package id.ac.ui.cs.friendservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserIdListDTO {
    List<String> userIds;
}

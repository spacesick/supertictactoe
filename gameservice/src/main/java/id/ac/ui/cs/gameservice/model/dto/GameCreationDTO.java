package id.ac.ui.cs.gameservice.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCreationDTO {

    List<String> playerUserIds;

    List<String> playerUsernames;

    int boardSize;

    int winLength;
}

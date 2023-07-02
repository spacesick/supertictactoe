package id.ac.ui.cs.supertictactoe.model.dto;

import java.util.List;

import org.springframework.data.util.Pair;

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

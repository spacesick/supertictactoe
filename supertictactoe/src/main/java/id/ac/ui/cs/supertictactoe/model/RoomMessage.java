package id.ac.ui.cs.supertictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomMessage {

    String player2;

    boolean started;

    String url;
}

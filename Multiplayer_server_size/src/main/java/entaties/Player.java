package entaties;

import java.io.DataOutputStream;
import java.io.IOException;

public class Player extends Entity{

	public Player(int pos, String nickname) {
		super(pos, nickname);
	}
	
	public void enviar_para_novo_player(DataOutputStream out) throws IOException {
		out.writeInt(0);
		out.writeInt(pos);
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(z);
		out.writeInt(life);
		out.writeInt(state);
		out.writeInt(index);
		out.writeInt(skin);
	}

}

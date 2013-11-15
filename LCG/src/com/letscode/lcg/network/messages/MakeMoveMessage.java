package com.letscode.lcg.network.messages;

import com.letscode.lcg.enums.CommandType;

public class MakeMoveMessage extends MessageBase {
	public CommandType what;
	public Integer row;
	public Integer col;
}

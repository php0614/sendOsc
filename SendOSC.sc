SendOSC{
	var net, path_;
	var <>numChan = 1, synthDefineRandomNumsIn, <>ampfollowerAtkTime = 0.01, <>ampfollowerRelTime = 0.01;

	*new {|path, netPort, netAddress="localhost"|
		^super.new.init(path, netPort, netAddress);
	}

	init { |path, netPort, netAddress|
		 net = NetAddr(netAddress, netPort);
		path_ = path;


		synthDefineRandomNumsIn = 10000.rand.asString;

}

	snd{ |val|
		                              //* = performlist
		net.sendMsg(path_, *val);
	}

	signalSnd{|busInput, netPort, netAddress = "localhost", prefix = 0|
		var s = Server.local;
		var localNet = NetAddr("localhost", NetAddr.langPort);
		var sendNet = NetAddr(netAddress, netPort);


		SynthDef("SendOSC_SignalSnd", {arg in = 0;
		var snd;
		snd = SendReply.kr(Impulse.kr(60), '/SendReply'++ synthDefineRandomNumsIn.asSymbol, Amplitude.ar(In.ar(in, numChan), ampfollowerAtkTime, ampfollowerRelTime));
			}).send(s);

   {
		Synth.after(s,"SendOSC_SignalSnd", args: [\in, busInput]);

		OSCFunc.newMatching({ |msg|  sendNet.sendMsg(path_, msg[3].round(0.000001))}, '/SendReply'++ synthDefineRandomNumsIn.asSymbol);

   }.defer(0.05);

}
}

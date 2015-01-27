package timer;




public class TimedBreaks /*extends Activity*/ {
	static int timeLimit;
	static int isBreak;
	

	

	public void getTimeLimit(int tL) {
		timeLimit = tL;

	}

	public String breakMessages(double timeRemain) {
		
		String iS = "iggy";
		switch (timeLimit) {
		case 0:
			timeLimit = 8;
			
			// " 4 hours "// pretends to be case 8
			// time4Count= 14400000;
			if (timeRemain == (210*60)) {//start of first break
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (200*60)) {//end of first break
				setBreak(1);
				return "Breaks Over, if you have another /n " +
						"subject you should take a look at it";

			}if (timeRemain == (180*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (170*60)) {//end of first break
				setBreak(1);
				return "Breaks Over, if you have another /n " +
						"subject maybe you should take a look at it";

			}if (timeRemain == (140*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (130*60)) {//end of the short break
				setBreak(1);
				return "Let's get back to studying./n subject maybe you should take a look at it";

			}if (timeRemain == (100*60)) {
				setBreak(0);
				return "That was alot of studying, let's take a break";

			}if (timeRemain == (90*60)) {
				setBreak(1);
				return "Let's get back to studying./n If you have another subject maybe you should take a look at it";
			}if (timeRemain == (60*60)) {
				setBreak(0);
				return "That was a lot of studying, /n let's take a break!!";
				//
			}if (timeRemain == (50*60)) {
				setBreak(1);
			return "Let's get back to the books. If you have another subject maybe you should take a look at it";
								//
			}if (timeRemain == (25*60)) {
				setBreak(0);
				return "Let's take a 5 mintue break.";
				//
			}if (timeRemain == (20*60)) {
				setBreak(1);
				return "Let's finish up studying and use the rest of this time for reviewing";
				//
			}
			break;
		case 1:
			// time4Count= 1800000; sett for a mintue
			// time4Count= 60000;
			
			if (timeRemain == 1750) {
				return "Let's study for the full 30 mintues";}
			/*	if (timeRemain == 1798) {
				return "Let's study for the full 30 mintues";}
if (timeRemain == 30) {
	return "will it happen";}*/
				
			

			break;
		case 2:
			// .setText("1 hour ");
			// time4Count= 3600000;
			if (timeRemain == (30*60)) {
				setBreak(0);
				return "Time for your first break.";

			}if (timeRemain == (25*60)) {
				setBreak(1);
				return "Time to get back to studying." +
						"/n Maybe you should look over another subject";

			}


			break;
		case 3:
			// .setText("1 hr 30 mins");
			// time4Count= 5400000;
			if (timeRemain == (60*60)) {
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (53*60)) {
				setBreak(1);
				return "Break's over, if you have another /n " +
						"subject you should probably take a look at it";

			}if (timeRemain == (26*60)) {
				setBreak(0);
				return "Let's take a second break ";

			}if (timeRemain == (24*60)) {
				setBreak(1);
				return "Time for the final push. /n Let's Give It Our Best!!";

			}




			break;
		case 4:
			// .setText(" 2 hours");
			// time4Count= 7200000;
			if (timeRemain == (90*60)) {//start of first break
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (80*60)) {//end of first break
				setBreak(1);
				return "Breaks over. If you have another /n " +
						"subject you should probably take a look at it";

			}if (timeRemain == (50*60)) {//start of second break
				setBreak(0);
				return "That was a lot of studying, time for another break ";

			}if (timeRemain == (43*60)) {//end of the short break
				setBreak(1);
				return "Let's get back to studying. If you have another /n " +
						"subject you should probably take a look at it";

			}if (timeRemain == (19*60)) {
				setBreak(0);
				return "Let's take another break";

			}if (timeRemain == (13*60)) {
				setBreak(1);
				return "Break's over. After all that studying \n  lightly reviewing the work you completed might be best";

			}

			break;
		case 5:
			
			// .setText("2 hr 30 mins");
			// time4Count=9000000;or 150minutes
			if (timeRemain == (120*60)) {//start of first break
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (110*60)) {//end of first break
				setBreak(1);
				return "Breaks Over, if you have another /n " +
						"subject you should take a look at it";

			}if (timeRemain == (80*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (70*60)) {//end of the short break
				setBreak(1);
				return "Let's get back to studying. /n Do you have another subject you'd like to study instead";

			}if (timeRemain == (45*60)) {
				setBreak(0);
				return "Let's take another break";

			}if (timeRemain == (35*60)) {
				setBreak(1);
				return "Time to get back to the books./n Do you have another subject you'd like to study instead";
//
			}if (timeRemain == (15*60)) {
				setBreak(0);
				return "That was a lot of studying, let's take a break";

			}if (timeRemain == (9*60)) {
				setBreak(1);
				return "Maybe we should use this time to review!!";

			}
			break;
		case 6:
			// .setText("3 hours");
			// time4Count= 10800000;
			if (timeRemain == (150*60)) {//start of first break
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (140*60)) {//end of first break
				setBreak(1);
				return "Breaks Over. If you have another /n " +
						"subject maybe you should take a look at it";

			}if (timeRemain == (110*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (100*60)) {//end of the short break
				setBreak(1);
				return "Let's get back to studying /n are you going to switch subjects?";

			}if (timeRemain == (70*60)) {
				setBreak(0);
				return "Let's take another break";

			}if (timeRemain == (60*60)) {
				setBreak(1);
				return "Breaks Over. If you have another /n " +
				"subject maybe you should take a look at it";
//
			}if (timeRemain == (30*60)) {
				setBreak(0);
				return "That was a lot of studying /n Let's take another break";
				//
			}if (timeRemain == (20*60)) {
				setBreak(1);
			return "breaks up!! Why not /n use the last of this time to review the material";
								//
			}
		case 7:
			// .setText("3 hrs 30 mins");
			// time4Count= 12600000;
			if (timeRemain == (180*60)) {//start of first break
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (170*60)) {//end of first break
				setBreak(1);
				return "Breaks Over, if you have another /n " +
						"subject maybe you should take a look at it";

			}if (timeRemain == (140*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (130*60)) {//end of the short break
				setBreak(1);
				return "Let's get back to studying./n subject maybe you should take a look at it";

			}if (timeRemain == (100*60)) {
				setBreak(0);
				return "That was alot of studying, let's take a break";

			}if (timeRemain == (90*60)) {
				setBreak(1);
				return "Let's get back to studying./n If you have another subject maybe you should take a look at it";
			}if (timeRemain == (60*60)) {
				setBreak(0);
				return "That was a lot of studying, /n let's take a break!!";
				//
			}if (timeRemain == (50*60)) {
				setBreak(1);
			return "Let's get back to the books. If you have another subject maybe you should take a look at it";
								//
			}if (timeRemain == (25*60)) {
				setBreak(0);
				return "Let's take a 5 mintue break.";
				//
			}if (timeRemain == (20*60)) {
				setBreak(1);
				return "Let's finish up studying and use the rest of this time for reviewing";
				//
			}
			
			break;
		case 8:
			// .setText("4 hrs ");
			// time4Count= 14400000; 
			if (timeRemain == (210*60)) {//start of first break
				setBreak(0);
				return "Time for your first break. ";

			}if (timeRemain == (200*60)) {//end of first break
				setBreak(1);
				return "Breaks Over, if you have another /n " +
						"subject you should take a look at it";

			}if (timeRemain == (180*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (170*60)) {//end of first break
				setBreak(1);
				return "Breaks Over, if you have another /n " +
						"subject maybe you should take a look at it";

			}if (timeRemain == (140*60)) {//start of second break
				setBreak(0);
				return "Time for another break ";

			}if (timeRemain == (130*60)) {//end of the short break
				setBreak(1);
				return "Let's get back to studying./n subject maybe you should take a look at it";

			}if (timeRemain == (100*60)) {
				setBreak(0);
				return "That was alot of studying, let's take a break";

			}if (timeRemain == (90*60)) {
				setBreak(1);
				return "Let's get back to studying./n If you have another subject maybe you should take a look at it";
			}if (timeRemain == (60*60)) {
				setBreak(0);
				return "That was a lot of studying, /n let's take a break!!";
				//
			}if (timeRemain == (50*60)) {
				setBreak(1);
			return "Let's get back to the books. If you have another subject maybe you should take a look at it";
								//
			}if (timeRemain == (25*60)) {
				setBreak(0);
				return "Let's take a 5 mintue break.";
				//
			}if (timeRemain == (20*60)) {
				setBreak(1);
				return "Let's finish up studying and use the rest of this time for reviewing";
				//
			}
			break;
		case 9:
			// .setText("30 mins");//pretends to be case 1
			// time4Count= 1800000;
			if (timeRemain == 1798) {
				return "Let's study for the full 30 mintues";}
			break;
		default:
			break;

		}
		return iS;
	}
	private void setBreak(int is){//0 is a break 1 is not
	//	Log.d("TimedBreak","setting isBreak"+is);
		isBreak = is;
	}
protected int isBreak(){
	//Log.d("TimedBreak","is it a break?"+isBreak);
	return isBreak;
}
	
}

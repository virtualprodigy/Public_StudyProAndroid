package com.virtualprodigy.studypro;



import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends Activity {

Button nextImage,perviousImage,closeButton;
CheckBox doNotShowAgain;
ImageView currentImage;
static int imageNumber = 1;
TextView tutorialText,pageNumber;
static int checkBoxValue;
private SharedPreferences tutorialPrefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		nextImage =(Button)findViewById(R.id.nextImage);
		perviousImage =(Button)findViewById(R.id.prevImage);
		closeButton =(Button)findViewById(R.id.closeHow);
		doNotShowAgain =(CheckBox)findViewById(R.id.doNotShowAgain);
		currentImage =(ImageView)findViewById(R.id.currentImage);
		tutorialText =(TextView)findViewById(R.id.tutText);
		pageNumber = (TextView)findViewById(R.id.pageNumber);
		pageNumber.setText("1/10");

		tutorialPrefs = getApplicationContext().getSharedPreferences("showHow", MODE_PRIVATE);
		checkBoxValue = tutorialPrefs.getInt("showHow", 0);
		//Log.d("checkbox",""+tutorialPrefs.getInt("showHow", 0));
		//checkBoxValue = getIntent().getIntExtra("checkVaule", 0);
		if (checkBoxValue == 1) {
			doNotShowAgain.setChecked(true);
		}
		if (checkBoxValue == 0) {
			doNotShowAgain.setChecked(false);
		}
		currentImage.setImageResource(R.raw.one);
		tutorialText.setText("Welcome to CramSlam. \n This Tutorial will teach how to get the most from this study aid.You can always review the directions by clicking the \"i\" ic_launcher at the bottom of the screen");
		
		
		nextImage.setOnClickListener(new View.OnClickListener() {

			
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				imageNumber =imageNumber+1;
				displayImage();
			}

		});
	
		perviousImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageNumber =imageNumber-1;
				displayImage();

			}

		});
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				finish();
			}

		});
		
		doNotShowAgain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if (doNotShowAgain.isChecked() == true) {
					checkBoxValue = 1;

				}
				if (doNotShowAgain.isChecked() == false) {

					checkBoxValue = 0;

				}
			}

		});
		
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences.Editor edit = tutorialPrefs.edit();
		edit.putInt("showHow", checkBoxValue);
		edit.commit();
		//Log.d("checkbox",""+tutorialPrefs.getInt("showHow", 0));
	}

	public void displayImage(){
		if(imageNumber ==-1){
			imageNumber =10;
		}if(imageNumber ==11){
			imageNumber =1;
		}
		
		switch (imageNumber){
		
		case 1:
			currentImage.setImageResource(R.raw.one);
			tutorialText.setText("Welcome to CramSlam. \n This Tutorial will teach how to get the most from this study aid.You can always review the directions by clicking the \"i\" ic_launcher at the bottom of the screen.");
			pageNumber.setText("1/10");
			break;
		
		case 2:
			currentImage.setImageResource(R.raw.two);
			tutorialText.setText("CramSlam features a study timer that can be set for a maxium peroid of 4 hours. ");
			pageNumber.setText("2/10");
			break;
		case 3:
			currentImage.setImageResource(R.raw.three);
			tutorialText.setText("Through the study period CramSlam will suggest taking breaks and switching between subjects. ");
			pageNumber.setText("3/10");
			break;
		case 4:
			currentImage.setImageResource(R.raw.four);
			tutorialText.setText("CramSlam also features a notepad  where you can store your homework related notes.");
			pageNumber.setText("4/10");
			break;
		case 5:
			currentImage.setImageResource(R.raw.five);
			tutorialText.setText("CramSlam also comes with a Grade-Calculator.");
			pageNumber.setText("5/10");
			break;
		case 6:
			currentImage.setImageResource(R.raw.six);
			tutorialText.setText("User's may choose between a weighted or unweighted grading system. The unweighted grade option simply averages the entered scores. While the weighted grade option asks for the users to enter a percentage corrsponding to each grade.");
			pageNumber.setText("6/10");
			break;
		case 7:
			currentImage.setImageResource(R.raw.seven);
			tutorialText.setText("When calculating a weighted grade, you may need to average sereval grades and apply one percentage to them. Long pressing an \"Enter Grade\" cell will make this option avaible for that cell.");
			pageNumber.setText("7/10");
			break;
		case 8:
			currentImage.setImageResource(R.raw.eight);
			tutorialText.setText("Entering each grade seperated by a comma and pressing the calculate button in the pop up window will average the grades.");
			pageNumber.setText("8/10");
			break;
		case 9:
			currentImage.setImageResource(R.raw.nine);
			tutorialText.setText("The average will be displayed before placing it into the cell. The user is also presented with the option to reenter the grades thus recalculating their average before placing it into the cell.");
			pageNumber.setText("9/10");
			break;
		case 10:
			currentImage.setImageResource(R.raw.ten);
			tutorialText.setText("Pressing the \"Add More Grades\" button will store you current entries and allow you to enter more grades. Pressing the \"Calculate Grade\" button at anytime will calculate your current grade.");
			pageNumber.setText("10/10");
			break;
			
			default:
				break;
		}
		
		
	}
	
	
	
	@Override
	public void finish() {
		//StudyTimer.displayHowCheckBox(checkBoxValue);
		// TODO Auto-generated method stub
		SharedPreferences.Editor edit = tutorialPrefs.edit();
		edit.putInt("showHow", checkBoxValue);
		edit.commit();
		//Log.d("checkbox",""+tutorialPrefs.getInt("showHow", 0));
		super.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tutorialPrefs = getApplicationContext().getSharedPreferences("showHow", MODE_PRIVATE);
		checkBoxValue = tutorialPrefs.getInt("showHow", 0);
	}

}

package grades;

import java.text.DecimalFormat;
import java.util.LinkedList;

import com.CramSlam.VirtualProdigy.R;
import com.CramSlam.VirtualProdigy.settingmenubuttons;
import com.CramSlam.VirtualProdigy.R.array;
import com.CramSlam.VirtualProdigy.R.id;
import com.CramSlam.VirtualProdigy.R.layout;
import com.CramSlam.VirtualProdigy.R.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;

public class CalcGrade extends Activity {
	
	 private static final String TAG = null;
	EditText g1,p1,g2,p2,g3,p3,g4,p4,g5,p5,g6,p6;
     Spinner spin ;
     boolean weightSpin = true;
	 
	LinkedList grade = new LinkedList();
	LinkedList percent = new LinkedList();
	LinkedList mUnweight = new LinkedList();
	static AlertDialog alert;
	AlertDialog.Builder builder;
	View hold;
	
	double numNodes;
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	   
	    setContentView(R.layout.calcgrades);
	        
	        g1 = (EditText)findViewById(R.id.editText1);//1       
	        p1 = (EditText)findViewById(R.id.editText2);//2
	        
	        g2 = (EditText)findViewById(R.id.editText3);//3
	        p2 = (EditText)findViewById(R.id.editText4);//4

	        g3 = (EditText)findViewById(R.id.editText5);//5
	        p3 = (EditText)findViewById(R.id.editText6);//6
	        
	        g4 = (EditText)findViewById(R.id.editText8);//7
	        p4 = (EditText)findViewById(R.id.editText7);//8
	        
	        g5 = (EditText)findViewById(R.id.editText11);//9
	        p5 = (EditText)findViewById(R.id.editText10);//10
	        
	        g6 = (EditText)findViewById(R.id.editText111);//11
	        p6 = (EditText)findViewById(R.id.editText112);//12
	       // String s = spin.getSelectedItem().toString();
	       
	       //Log.d(TAG,"register the text boxes for context menus");
	        this.registerForContextMenu(g1);
	        this.registerForContextMenu(g2);
	        this.registerForContextMenu(g3);
	        this.registerForContextMenu(g4);
	        this.registerForContextMenu(g5);
	        this.registerForContextMenu(g6);
	       
	        //Log.d(TAG,"spinner set and listeners");
	        spin = (Spinner) findViewById(R.id.spinGrade); 
		    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		            this, R.array.style_array, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    spin.setAdapter(adapter);
		    spin.setOnItemSelectedListener(new MyOnItemSelectedListener());

		  
	        Button saveAdd,calculate,info;
			
			info = (Button)findViewById(R.id.infobuttonGradepage);
	        
	        saveAdd=(Button)findViewById(R.id.buttonsave);
	        calculate = (Button) findViewById(R.id.buttoncalculate);
	        
	        
	        saveAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					 if(weightSpin == true){
					storeValues();
					}if(weightSpin == false){
						 saveValues();
					}
					 
				}
			});
	       
	        calculate.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					 if(weightSpin == true){
					 storeValues();
					 calculate();
					 }if(weightSpin== false){
						 saveValues();
						 calculateUnWeighted();
					 }
				}
			});
	        
	        

			info.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), settingmenubuttons.class));
					
				}
			});
	        
	      	
	  
	  }
	  
	  
	  @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.settings:
				startActivity(new Intent(getApplicationContext(), settingmenubuttons.class));
				//startActivity(new Intent(this, Prefs.class));
				return true;
			}

			return false;
		}
	  
	  public class MyOnItemSelectedListener implements OnItemSelectedListener {//the listener for the spin selection

		    public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
		    	
		    	if(parent.getItemAtPosition(pos).toString().equals("Weighted Grade")){
		    		weightSpin= true;
		    		 clearGradeList();
		    	}else if(parent.getItemAtPosition(pos).toString().equals("Unweighted Grade")){
		    		weightSpin = false;
		    		clearGradeList();
		    	}
		    	setText();		    		
		    	/* Toast.makeText(parent.getContext(),
		    	          parent.getItemAtPosition(pos).toString()+" Mode "+weightSpin, Toast.LENGTH_LONG).show();*/
		    	 
		    	 
		    }

		    public void onNothingSelected(AdapterView parent) {
		      
		    }
		}
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo )//context menus for text edits
      {
		  menu.clearHeader();
		  menu.clear();
		   
		   if(weightSpin == true){
           super.onCreateContextMenu(menu, v, menuInfo);  
           MenuInflater inflate = getMenuInflater();
           inflate.inflate(R.menu.grademenu, menu);
           conCalled(v);
		   }
            
      }
	  
	  @Override
	  public boolean onContextItemSelected(MenuItem item){//context appears for textedits
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  
		  switch (item.getItemId()){
		  case R.id.mutligrade:
			  contextUnweight();
		   return true;
		   default:
			   return super.onContextItemSelected(item);
			  
		  }
		  
	  }
	  
	  protected void storeValues(){//saves the grades for the weighted grade spin
	  if (!g1.getText().toString().equals("")&& !p1.getText().toString().equals("")){//will crash if there are spaces
		 // grade.add((Double.valueOf(g1.getText().toString())).doubleValue());
		  grade.add(g1.getText().toString());
		  numNodes = numNodes +1;
		 // percent.add((Double.valueOf(p1.getText().toString())).doubleValue());
		 percent.add(p1.getText().toString());
	  }
	  
	 if (!g2.getText().toString().equals("")&& !p2.getText().toString().equals("")){
		  grade.add(g2.getText().toString());
		  numNodes = numNodes +1;
		 percent.add(p2.getText().toString());
	  }
	 
	 
	 
	 if (!g3.getText().toString().equals("")&& !p3.getText().toString().equals("")){
		  grade.add(g3.getText().toString());
		  numNodes = numNodes +1;
		 percent.add(p3.getText().toString());
	  }
	 
	 
	 if (!g4.getText().toString().equals("")&& !p4.getText().toString().equals("")){
		  grade.add(g4.getText().toString());
		  numNodes = numNodes +1;
		 percent.add(p4.getText().toString());
	  }
	 
	 
	 
	 if (!g5.getText().toString().equals("")&& !p5.getText().toString().equals("")){
		  grade.add(g5.getText().toString());
		  numNodes = numNodes +1;
		 percent.add(p5.getText().toString());
	  }
	 
	 
	 if (!g6.getText().toString().equals("")&& !p6.getText().toString().equals("")){
		  grade.add(g6.getText().toString());
		  numNodes = numNodes +1;
		 percent.add(p6.getText().toString());
	  }
	  
	  
	  
	  g1.setText("");
	  p1.setText("");
	  
	  g2.setText("");
	  p2.setText("");
	  
	  g3.setText("");
	  p3.setText("");
	  
	  g4.setText("");
	  p4.setText("");
	  
	  g5.setText("");
	  p5.setText("");
	  
	  g6.setText("");
	  p6.setText("");
		  
	  }
	  protected void calculate(){//calculates grade for the weighted grade
		double gTotal=0;
		double g=0;
		double p = 0;
		if(grade.size()!=0){
		for(;numNodes>0;numNodes--){
			
		
			
			g= (Double.valueOf(grade.remove().toString()).doubleValue() );
			p=(Double.valueOf(percent.remove().toString()).doubleValue() )/100.0;
			
			gTotal=gTotal+(g * p);
		}
		  
		  builder = new AlertDialog.Builder(this);
			builder.setMessage(""+gTotal).setCancelable(false).setPositiveButton(
					"Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							
							
						}
					});
			
			alert=builder.create();
			alert.show();
			
			numNodes=0;
			gTotal=0;
		}
	  }
	  protected void contextUnweight(/*View view*/){//this parses the the string recieve in the alert dialog for mutlipe grade unweight input in the wieghted grade spin
		
		  final EditText numInput = new EditText(this);
		  
		  new AlertDialog.Builder(this)
		  .setTitle("Enter Grades seperated by \",\" ")
		 .setView(numInput)
		 
		  
		  .setPositiveButton("Calculate",new DialogInterface.OnClickListener(){
			  public void onClick(DialogInterface dialog, int id) {
				  mUnweight.add( numInput.getText().toString());
				  CalculateUnweighted(mUnweight);
			  }
		  })
		  .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
			  public void onClick(DialogInterface dialog, int id) {
			  
			  }
		  })
		  
		  .show();
		  
		 
			  
		  
		  
	  }
	  
	 protected void CalculateUnweighted(LinkedList list){//this is for the context menu calculations
		  
		  String nums;
		  nums = list.remove().toString();
		  String delim= "[,]";
		  String[] numTokens = nums.split(delim);
		  
		  double i = 0,total= 0, current;
		  
		 
		  for(; i<numTokens.length;i++){
			  current= (Double.valueOf(numTokens[(int) i]).doubleValue() );
			  total =total+current;  
			
		  }
		  
		  total=total/i;
		 
	        DecimalFormat df = new DecimalFormat("#.##");
	        
		  
		  EditText t;
		  t=(EditText) getconCalled();
		  t.setText(""+df.format(total)); 
		  
		  new AlertDialog.Builder(this)
		  .setTitle("Grade")
		 .setMessage(""+df.format(total))
		 
		 	  
		  .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
			  public void onClick(DialogInterface dialog, int id) {
				  
			  }
		  })
		  .setNegativeButton("Redo",new DialogInterface.OnClickListener(){
			  public void onClick(DialogInterface dialog, int id) {
				  contextUnweight();
			  }
		  })
		  
		  .show();
		  
		  
	  }
	 public void conCalled(View v){//saves the view when context menu is selected
		 hold = v;
	 }
	 public View getconCalled(){//finds the view when context menu selected
		 return hold;
	 }
	 
	 protected void setText(){
		  if(weightSpin ==true){
		    	((TextView) findViewById(R.id.textView1))
				.setText("Enter Grade");
		    	((TextView) findViewById(R.id.textView2))
				.setText("Enter Percent");
		    	
		    }if(weightSpin == false){
		    	((TextView) findViewById(R.id.textView1))
				.setText("Enter Grade");
		    	((TextView) findViewById(R.id.textView2))
				.setText("Enter Grade");	
		    }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 protected void saveValues(){//saves grade list for the unweighted grade spin
		  if (!g1.getText().toString().equals("")){
			  grade.add(g1.getText().toString());
			  numNodes = numNodes +1;
		  }
		  if(!p1.getText().toString().equals("")){
			 grade.add(p1.getText().toString());
			 numNodes = numNodes +1;
		  }
		  
		  
		  if (!g2.getText().toString().equals("")){
			  grade.add(g2.getText().toString());
			  numNodes = numNodes +1;
		  }
		  if(!p2.getText().toString().equals("")){
			 grade.add(p2.getText().toString());
			 numNodes = numNodes +1;
		  }
		  
		  
		  if (!g3.getText().toString().equals("")){
			  grade.add(g3.getText().toString());
			  numNodes = numNodes +1;
		  }
		  if(!p3.getText().toString().equals("")){
			 grade.add(p3.getText().toString());
			 numNodes = numNodes +1;
		  }
		  
		  if (!g4.getText().toString().equals("")){
			  grade.add(g4.getText().toString());
			  numNodes = numNodes +1;
		  }
		  if(!p4.getText().toString().equals("")){
			 grade.add(p4.getText().toString());
			 numNodes = numNodes +1;
		  }
		  
		  if (!g5.getText().toString().equals("")){
			  grade.add(g5.getText().toString());
			  numNodes = numNodes +1;
		  }
		  if(!p5.getText().toString().equals("")){
			 grade.add(p5.getText().toString());
			 numNodes = numNodes +1;
		  }
		  
		  if (!g6.getText().toString().equals("")){
			  grade.add(g6.getText().toString());
			  numNodes = numNodes +1;
		  }
		  if(!p6.getText().toString().equals("")){
			 grade.add(p6.getText().toString());
			 numNodes = numNodes +1;
		  }
		  
		  
		  g1.setText("");
		  p1.setText("");
		  
		  g2.setText("");
		  p2.setText("");
		  
		  g3.setText("");
		  p3.setText("");
		  
		  g4.setText("");
		  p4.setText("");
		  
		  g5.setText("");
		  p5.setText("");
		  
		  g6.setText("");
		  p6.setText("");
			  
		  }
	 
		  protected void calculateUnWeighted(){//calculates  the grade for unweighted selection spin
			double gTotal=0;
			double g=0;
			double div = numNodes;
			
			if(grade.size()!=0){
			
			for(;numNodes>0;numNodes--){
				g = (Double.valueOf(grade.remove().toString()).doubleValue() );
				gTotal=gTotal+g;
			}
			
			gTotal = gTotal/div;
			  
			  builder = new AlertDialog.Builder(this);
				builder.setMessage(""+gTotal).setCancelable(false).setPositiveButton(
						"Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								
							}
						});
				
				alert=builder.create();
				alert.show();
				
				numNodes=0;
				gTotal=0;
			}
		  }
	 
	 
	 protected void clearGradeList(){//clears the list for trans between weighted and unweighted
		 numNodes = 0;
		 grade.clear();
	 }
	 
	 
	 
	 
}
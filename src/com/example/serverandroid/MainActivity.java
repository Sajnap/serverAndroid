package com.example.serverandroid;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private final static Random random = new Random();
	ArrayList<String> adviceList;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adviceList=new ArrayList<String>();
		adviceList.add("Take smaller bites");
		adviceList.add("Go for the tight jeans. No they do NOT make you look fat.");
		adviceList.add("One word: inappropriate");
		adviceList.add("Just for today, be honest. Tell yourboss what you *really* think");
		adviceList.add("You might want to rethink that haircut.");

		adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,adviceList);

		((ListView)findViewById(R.id.listView1)).setAdapter(adapter);
		new MyAsyncTask().execute();
	}

	public void addClicked(View v) {
		EditText edit_advice=(EditText)findViewById(R.id.editText1);
		adviceList.add(edit_advice.getText().toString());
		adapter.notifyDataSetInvalidated();		
		adapter.notifyDataSetChanged();
	}
	public  class MyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			Toast.makeText(getApplicationContext(), "Starting to Server Service", Toast.LENGTH_SHORT).show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), "Finished Server Service"+result, Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... params) {
			try{

				ServerSocket serverSocket = new ServerSocket(8888);
				while(!serverSocket.isClosed()) {
					System.out.println("Waiting for connection");
					Socket socket = serverSocket.accept();
					System.out.println("Connected");

					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					String advice = getAdvice();
					System.out.println("Sending advice: " + advice);
					writer.write(advice);
					writer.close();
					System.out.println("Advice sent!");
					socket.close();
				}
				serverSocket.close();

			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}

		private String getAdvice() {
			return adviceList.get(random.nextInt(adviceList.size()));
		}




	}

}

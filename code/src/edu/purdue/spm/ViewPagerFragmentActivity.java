/**
 * 
 */
package edu.purdue.spm;

import java.util.List;
import java.util.Vector;

import edu.purdue.spm.adapter.PagerAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;




/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager  
 */
public class ViewPagerFragmentActivity extends FragmentActivity{
	/** maintains the pager adapter*/
	private PagerAdapter mPagerAdapter;
	
	private final String dbname = "test.db";
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.viewpager_layout);
		//Initialize the pager
		this.initializePaging();
	}
	
	/**
	 * Initialize the fragments to be paged
	 */
	private void initializePaging() {
		
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, ContactBookFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, MainDashBoardFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, GroupDashBoardFragment.class.getName()));
		this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		//
		ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPagerAdapter);
		pager.setCurrentItem(1);	// Set the default page to Main Dash Board
	}
	
	private SQLiteDatabase db;

	public SQLiteDatabase getDB(){
			db = openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
			return db;
		}
		
		public void closeDB(){
			db.close();
		}
		
		public void clearTaskTable(){
			db = openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
			db.execSQL("DROP TABLE IF EXISTS task");
			db.execSQL("CREATE TABLE task (" +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"parentTaskId VARCHAR, " +
					"taskId VARCHAR, " +
					"ownerId VARCHAR, " +
					"title VARCHAR, " +
					"description VARCHAR, " +
					"dueTime LONG, " +
					"timeStamp LONG, " +
					"progress INT, " +
					"type INT, " +
					"weight INT, " +
					"depth INT" +
					")");
			db.close();
		}

}

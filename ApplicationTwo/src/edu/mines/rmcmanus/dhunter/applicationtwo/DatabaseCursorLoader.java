/**
 * Description:  This class was acquired from the demo code in class.  Instead of "reinventing
 * the wheel" we decided to reuse the code base.
 * 
 * @author Randy Bower
 */

package edu.mines.rmcmanus.dhunter.applicationtwo;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DatabaseCursorLoader extends AsyncTaskLoader<Cursor>
{
  SQLiteOpenHelper dbHelper = null;
  String rawQuery = null;
  String[] args = null;
  Cursor lastCursor = null;

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
public DatabaseCursorLoader( Context context )
  {
    super( context );
    Log.d( "ToDo: " + this.getClass().getName(), "DatabaseCursorLoader() ... " + "Thread ID: " + Thread.currentThread().getId() );
  }

  /**
   * Creates a fully-specified SQLiteCursorLoader.
   * 
   * See http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#rawQuery(java.lang.String, java.lang.String[])
   * for documentation on the meaning of the parameters. These will be passed as-is to that call.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
public DatabaseCursorLoader( Context context, SQLiteOpenHelper dbHelper, String rawQuery, String[] args )
  {
    super( context );
    Log.d( "ToDo: " + this.getClass().getName(), "DatabaseCursorLoader() ... " + rawQuery + " Thread ID: " + Thread.currentThread().getId() );

    this.dbHelper = dbHelper;
    this.rawQuery = rawQuery;
    this.args = args;
  }

  /**
   * Runs on a worker thread, loading data.
   */
  @Override
  public Cursor loadInBackground()
  {
    Log.d( "ToDo: " + this.getClass().getName(), "loadInBackground() ... " + "Thread ID: " + Thread.currentThread().getId() );

    Cursor cursor = this.dbHelper.getReadableDatabase().rawQuery( rawQuery, args );

    if( cursor != null )
    {
      cursor.getCount();  // Ensure the cursor is filled.
    }

    return cursor;
  }

  /**
   * Runs on the UI thread, routing the results from the background thread to
   * whatever is using the Cursor (e.g., a CursorAdapter).
   */
  @Override
  public void deliverResult( Cursor cursor )
  {
    Log.d( "ToDo: " + this.getClass().getName(), "deliverResult() ... " + "Thread ID: " + Thread.currentThread().getId() );

    if( isReset() )
    {
      // An async query came in while the loader is stopped.
      if( cursor != null )
      {
        cursor.close();
      }

      return;
    }

    Cursor oldCursor = lastCursor;
    lastCursor = cursor;

    if( isStarted() )
    {
      super.deliverResult( cursor );
    }

    if( oldCursor != null && oldCursor != cursor && !oldCursor.isClosed() )
    {
      oldCursor.close();
    }
  }

  /**
   * Starts an asynchronous load of the list data. When the result is ready the callbacks will be called on the UI thread.
   * If a previous load has been completed and is still valid the result may be passed to the callbacks immediately.
   * 
   * Must be called from the UI thread.
   */
  @Override
  protected void onStartLoading()
  {
    Log.d( "ToDo: " + this.getClass().getName(), "onStartLoading() ... " + "Thread ID: " + Thread.currentThread().getId() );

    if( lastCursor != null )
    {
      deliverResult( lastCursor );
    }

    if( takeContentChanged() || lastCursor == null )
    {
      forceLoad();
    }
  }

  /**
   * Must be called from the UI thread, triggered by a call to stopLoading().
   */
  @Override
  protected void onStopLoading()
  {
    Log.d( "ToDo: " + this.getClass().getName(), "onStopLoading() ... " + "Thread ID: " + Thread.currentThread().getId() );

    cancelLoad();  // Attempt to cancel the current load task if possible.
  }

  /**
   * Must be called from the UI thread, triggered by a call to cancel(). Here, we make sure our Cursor is closed,
   * if it still exists and is not already closed.
   */
  @Override
  public void onCanceled( Cursor cursor )
  {
    Log.d( "ToDo: " + this.getClass().getName(), "onCanceled() ... " + "Thread ID: " + Thread.currentThread().getId() );

    if( cursor != null && !cursor.isClosed() )
    {
      cursor.close();
    }
  }

  /**
   * Must be called from the UI thread, triggered by a call to reset(). Here, we make sure our Cursor is closed,
   * if it still exists and is not already closed.
   */
  @Override
  protected void onReset()
  {
    Log.d( "ToDo: " + this.getClass().getName(), "onReset() ... " + "Thread ID: " + Thread.currentThread().getId() );

    super.onReset();

    onStopLoading();  // Ensure the loader is stopped.

    if( lastCursor != null && !lastCursor.isClosed() )
    {
      lastCursor.close();
    }

    lastCursor = null;
  }

  /**
   * Writes semi-user-readable contents to supplied output.
   */
  @Override
  public void dump( String prefix, FileDescriptor fd, PrintWriter writer, String[] args )
  {
    Log.d( "ToDo: " + this.getClass().getName(), "dump() ... " + "Thread ID: " + Thread.currentThread().getId() );

    super.dump( prefix, fd, writer, args );
    writer.print( prefix );
    writer.print( "rawQuery=" );
    writer.println( rawQuery );
    writer.print( prefix );
    writer.print( "args=" );
    writer.println( Arrays.toString( args ) );
  }

  public void insert( String table, String nullColumnHack, ContentValues values )
  {
    new InsertTask( this ).execute( dbHelper, table, nullColumnHack, values );
  }

  public void update( String table, ContentValues values, String whereClause, String[] whereArgs )
  {
    new UpdateTask( this ).execute( dbHelper, table, values, whereClause, whereArgs );
  }

  public void replace( String table, String nullColumnHack, ContentValues values )
  {
    new ReplaceTask( this ).execute( dbHelper, table, nullColumnHack, values );
  }

  public void delete( String table, String whereClause, String[] whereArgs )
  {
    new DeleteTask( this ).execute( dbHelper, table, whereClause, whereArgs );
  }

  public void execSQL( String sql, Object[] bindArgs )
  {
    new ExecSQLTask( this ).execute( dbHelper, sql, bindArgs );
  }

  private abstract class ContentChangingTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
  {
    private Loader<?> loader = null;

    ContentChangingTask( Loader<?> loader )
    {
      this.loader = loader;
    }

    @Override
    protected void onPostExecute( Result param )
    {
      loader.onContentChanged();
    }
  }

  private class InsertTask extends ContentChangingTask<Object, Void, Void>
  {
    InsertTask( DatabaseCursorLoader loader )
    {
      super( loader );
    }

    @Override
    protected Void doInBackground( Object... params )
    {
      SQLiteOpenHelper db = (SQLiteOpenHelper)params[0];
      String table = (String)params[1];
      String nullColumnHack = (String)params[2];
      ContentValues values = (ContentValues)params[3];

      db.getWritableDatabase().insert( table, nullColumnHack, values );

      return null;
    }
  }

  private class UpdateTask extends ContentChangingTask<Object, Void, Void>
  {
    UpdateTask( DatabaseCursorLoader loader )
    {
      super( loader );
    }

    @Override
    protected Void doInBackground( Object... params )
    {
      SQLiteOpenHelper db = (SQLiteOpenHelper)params[0];
      String table = (String)params[1];
      ContentValues values = (ContentValues)params[2];
      String where = (String)params[3];
      String[] whereParams = (String[])params[4];

      db.getWritableDatabase().update( table, values, where, whereParams );

      return null;
    }
  }

  private class ReplaceTask extends ContentChangingTask<Object, Void, Void>
  {
    ReplaceTask( DatabaseCursorLoader loader )
    {
      super( loader );
    }

    @Override
    protected Void doInBackground( Object... params )
    {
      SQLiteOpenHelper db = (SQLiteOpenHelper)params[0];
      String table = (String)params[1];
      String nullColumnHack = (String)params[2];
      ContentValues values = (ContentValues)params[3];

      db.getWritableDatabase().replace( table, nullColumnHack, values );

      return null;
    }
  }

  private class DeleteTask extends ContentChangingTask<Object, Void, Void>
  {
    DeleteTask( DatabaseCursorLoader loader )
    {
      super( loader );
    }

    @Override
    protected Void doInBackground( Object... params )
    {
      SQLiteOpenHelper db = (SQLiteOpenHelper)params[0];
      String table = (String)params[1];
      String where = (String)params[2];
      String[] whereParams = (String[])params[3];

      db.getWritableDatabase().delete( table, where, whereParams );

      return null;
    }
  }

  private class ExecSQLTask extends ContentChangingTask<Object, Void, Void>
  {
    ExecSQLTask( DatabaseCursorLoader loader )
    {
      super( loader );
    }

    @Override
    protected Void doInBackground( Object... params )
    {
      SQLiteOpenHelper db = (SQLiteOpenHelper)params[0];
      String sql = (String)params[1];
      Object[] bindParams = (Object[])params[2];

      db.getWritableDatabase().execSQL( sql, bindParams );

      return null;
    }
  }
}

package com.example.UoA.healthconnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Nancy on 7/11/14.
 */
public class MyAlertDialogWIndow extends DialogFragment {

    /** Declaring the interface, to invoke a callback function in the implementing activity class */
    AlertPositiveListener alertPositiveListener;

    /** An interface to be implemented in the hosting activity for "OK" button click listener */
    interface AlertPositiveListener {
        public void onPositiveClick(boolean isDeleteSet);
    }

    /** This is a callback method executed when this fragment is attached to an activity.
     *  This function ensures that, the hosting activity implements the interface AlertPositiveListener
     * */
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try{
            alertPositiveListener = (AlertPositiveListener) activity;
        }catch(ClassCastException e){
            // The hosting activity does not implemented the interface AlertPositiveListener
            throw new ClassCastException(activity.toString() + " must implement AlertPositiveListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** Defining an event listener for the Yes button click */
        OnClickListener positiveClick = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity().getBaseContext(), "Application is quitting ...", Toast.LENGTH_SHORT).show();
                alertPositiveListener.onPositiveClick(true);
            }
        };

        /** Defining an event listener for the No button click */
        OnClickListener negativeClick = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity().getBaseContext(), "Returning to the main activity", Toast.LENGTH_SHORT).show();

            }
        };

        /** Creating a builder object for the AlertDialog*/
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        String message = getArguments().getString("message");

        /**  Setting the message */
        b.setMessage(message);

        /** Setting the Negative button */
        b.setNegativeButton("No", negativeClick);

        /** Setting the Positive button */
        b.setPositiveButton("Yes", positiveClick);

        /** Setting a title for the dialog */
        b.setTitle("Confirmation");

        /** Creating the AlertDialog , from the builder object */
        Dialog d = b.create();

        /** Returning the alert window */
        return d;
    }
}

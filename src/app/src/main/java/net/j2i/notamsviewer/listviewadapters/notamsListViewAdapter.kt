package net.j2i.notamsviewer.listviewadapters

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.j2i.notamsviewer.R
import net.j2i.notamsviewer.models.notam

public class notamsListViewAdapter: ArrayAdapter<notam> {
    private var context:Context

    constructor(context: Context,  arrayList:ArrayList<notam>):super(context, 0,arrayList) {
        this.context = context;

    }

    public override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var currentView = convertView
        if (currentView == null) {
            currentView = LayoutInflater.from(this.context).inflate(R.layout.listlayout_notam, parent, false)
        }

        val positionItem = getItem(position);

        positionItem?.let {
            val idTextView = currentView!!.findViewById<TextView>(R.id.notam_numbertext)
            idTextView.text = positionItem.id
            if(positionItem.id == "" || positionItem.id == null) {
                idTextView.text = "N/A"
            }

            val descriptionText = currentView!!.findViewById<TextView>(R.id.notam_description)
            descriptionText.text = positionItem.text
            if(positionItem.notamClass == "Domestic") {
                descriptionText.setTextColor(Color.GREEN)
            } else if (positionItem.notamClass == "International") {
                descriptionText.setTextColor(Color.BLUE)
            } else {
                descriptionText.setTextColor(Color.BLACK)
            }



            val startDate = currentView!!.findViewById<TextView>(R.id.notam_startdate)
            if(positionItem.effectiveStart != null) {
                startDate.text =positionItem.effectiveStart!!.toString()
            } else {
                startDate.text = ""
            }

            val endDate = currentView!!.findViewById<TextView>(R.id.notam_enddate)
            if(positionItem.effectiveEnd != null) {
                endDate.text = Html.fromHtml(positionItem.effectiveEnd!!.toString(), FROM_HTML_MODE_COMPACT )
            } else {
                endDate.text = ""
            }
        }
        return currentView!!
    }
}
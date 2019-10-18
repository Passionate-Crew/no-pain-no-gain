package com.passionatecrew.nopainnogain;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private List<String> listDataHeader;
    //Map contenant le header des ListView et les differents list d'items
    private HashMap<String, List<String>> listDataChild;
    //Map pour garder les etat des checkbox coch� ou non
    private HashMap<Integer, boolean[]> mChildCheckStates;
    
    //
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
 
    /**
     * Constrcteur
     * @param context
     * @param listDataHeader
     * @param listChildData
     */
    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        //Initialise la map des �tats
        this.mChildCheckStates = new HashMap<Integer, boolean[]>();
    }
 
    /**
     * Cette methode permet d'obtenir une valeur contenue dans une liste. Cette liste est elle-meme contenue dans la HashMap
     * @param groupPosition la position dans liste dans laquelle est contenue l'object
     * @parem childPosition la position dans la liste de l'object souhait�
     */
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    /**
     * Cette methode permet d'obtenir une View "item" avec un texte predefinis et un agencement adequat pour un item.
     * @param groupPosition la position dans liste dans laquelle est contenue l'object
     * @param childPosition la position dans la liste de l'object souhait�
     * @param isLastChild definie si c'est le dernier �l�ment d'une liste
     * @param convertView View qui definit � quoi le composant va ressembler (null ici)
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
 
    	final int mGroupPosition = groupPosition;
    	final int mChildPosition = childPosition;
    	
    	//recherche la valeur textuelle de l'item avec la methode getChild
        final String childText = (String) this.getChild(groupPosition, childPosition);
 
        //Si aucune view "model" (convertView) n'a �t� fourni, configure celui-ci par celui de base cr�er specialement pour les items (R.layout.list_item)
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
            
            //Initialise le childViewHolder
            this.childViewHolder = new ChildViewHolder();
            this.childViewHolder.mChildText = (TextView) convertView.findViewById(R.id.lblListItem);
            
            this.childViewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.check);
            
            convertView.setTag(R.layout.list_item, childViewHolder);
            
        } else {
        	this.childViewHolder = (ChildViewHolder) convertView.getTag(R.layout.list_item);
        }
        
        this.childViewHolder.mChildText.setText(childText);
        
        this.childViewHolder.mCheckBox.setOnCheckedChangeListener(null);
        
        if(this.mChildCheckStates.containsKey(mGroupPosition)) {
        	
        	boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
        	
        	this.childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);
        } else {
        	
        	boolean getChecked[] = new boolean[this.getChildrenCount(mGroupPosition)];
        	
        	mChildCheckStates.put(mGroupPosition, getChecked);
        	
        	this.childViewHolder.mCheckBox.setChecked(false);
        	
        }
        
        this.childViewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                        boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                        getChecked[mChildPosition] = isChecked;
                        mChildCheckStates.put(mGroupPosition, getChecked);
                } else {
                        boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                        getChecked[mChildPosition] = isChecked;
                        mChildCheckStates.put(mGroupPosition, getChecked);
                }
            }
        });
        
        //Permet de recuperer le textView et son agencement (configur� dans le layout list_item)
        //TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        //Modifie ce textView avec le texte trouv� au prealable
        //txtListChild.setText(childText);
        
        return convertView;
    }
 
    /**
     * Methode retournant combien de valeurs possede une List � une position donn�e
     * @param groupPosition position de la liste dans la HashMap
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }
 
    /**
     * Methode retournant le header d'une liste d'item
     * @param groupPosition position de la liste
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    /**
     * Cette methode permet d'obtenir une View "header" avec un texte predefinis et un agencement adequat pour un header.
     * @param groupPosition la position dans liste dans laquelle est contenue l'object
     * @param childPosition la position dans la liste de l'object souhait�
     * @param isLastChild definie si c'est le dernier �l�ment d'une liste
     * @param convertView View qui definit � quoi le composant va ressembler (null ici)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    	
    	//recherche la valeur textuelle du header avec la methode getGroup
        String headerTitle = (String) getGroup(groupPosition);
        
        //Si aucune view "model" (convertView) n'a �t� fourni, configure celui-ci par celui de base cr�er specialement pour les header (R.layout.list_group)
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
            
            //Initialise le groupViewHolder
            this.groupViewHolder = new GroupViewHolder();
            this.groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);
            convertView.setTag(this.groupViewHolder);
            
        } else {
        	this.groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        
        this.groupViewHolder.mGroupText.setTypeface(null, Typeface.BOLD);
        this.groupViewHolder.mGroupText.setText(headerTitle);
 
        //Permet de recuperer le textView et son agencement (configur� dans le layout list_item)
        //TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        //Modifie ce textView avec le texte trouv� au prealable et en caractere gras
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        //lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    public final class GroupViewHolder {
    	TextView mGroupText;
    }
    
    public final class ChildViewHolder {
    	
    	TextView mChildText;
    	CheckBox mCheckBox;
    }
}
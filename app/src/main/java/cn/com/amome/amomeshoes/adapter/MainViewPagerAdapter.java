/**
 * @Title:MainViewPagerAdapter.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-7 上午10:39:03
 */
package cn.com.amome.amomeshoes.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> fragmentsList;
	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public MainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.fragmentsList = list;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}

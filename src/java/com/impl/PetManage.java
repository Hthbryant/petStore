package com.impl;

import com.dao.BaseDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * @author 咕噜科
 * ClassName: PetManage
 * date: 2021/8/26 22:12
 * Description:
 * version 1.0
 */
public class PetManage extends BaseDAO {
    public void showAll(){
        showPetName();
        showPetOwner();
        showPetStore();
        login();
    }

    /**
     * 显示宠物的姓名以及ID方法
     */
    public void showPetName(){
        conn=getConnection();
        String sql="SELECT id,pet_name from pet";
        try {
            state=conn.prepareStatement(sql);
            rs=state.executeQuery();
            System.out.println("Wonderland醒来，所有宠物从MySQL中醒来");
            System.out.println("*************************************");
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("pet_name");
                System.out.println("第"+id+"只宠物,名字叫："+name);
            }
            System.out.println("*************************************\n");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeObject1();
        }

    }
    /**
     * 显示宠物主人方法
     */
    public void showPetOwner(){
        conn=getConnection();
        String sql="SELECT pet.id,username from pet,user_pet,user where pet.id=user_pet.pet_id and user_pet.user_id = user.id";
        try {
            state=conn.prepareStatement(sql);
            rs=state.executeQuery();
            System.out.println("所有宠物主人从MySQL中醒来");
            System.out.println("*************************************");
            while(rs.next()){
                int id=rs.getInt("pet.id");
                String name=rs.getString("username");
                System.out.println("第"+id+"只宠物主人,名字叫："+name);
            }
            System.out.println("*************************************\n");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeObject1();
        }
    }
    /**
     * 显示宠物商店方法
     */
    public void showPetStore(){
        conn=getConnection();
        String sql="SELECT id,store_name,store_address from pet_store";
        try {
            state=conn.prepareStatement(sql);
            rs=state.executeQuery();
            System.out.println("所有宠物商店从MySQL中醒来");
            System.out.println("*************************************");
            while(rs.next()){
                int id=rs.getInt("id");
                String storeName=rs.getString("store_name");
                String storeAddress=rs.getString("store_address");
                System.out.println("第"+id+"个商店的名字叫: "+storeName+" ,地址在: "+storeAddress);
            }
            System.out.println("*************************************\n");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeObject1();
        }
    }

    /**
     * 登录界面选择是主人登录还是商店登录方法
     */
    public void login(){

        System.out.println("请选择输入登录模式\n1.宠物主人登录\n2.宠物商店登录\n3.退出系统\n-------------------");
        try {
            Scanner input=new Scanner(System.in);
            int choise=input.nextInt();
            if(choise<1|| choise>3){
                System.out.println("输入有误，请重新选择");
                login();
            }else{
                switch (choise) {
                    case 1:
                        petOwnerLogin();
                        break;
                    case 2:
                        petStoreLogin();
                        break;
                    case 3:
                        System.out.println("谢谢使用");
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("输入有误，请重新选择");
            login();
        }

    }
    /**
     * 宠物主人登录方法
     * @return
     */
    public boolean petOwnerLogin(){
        boolean flag=false;
        try {
            Scanner input=new Scanner(System.in);
            System.out.println("请先登录，请您先输入主人的名字");
            String name=input.next();
            System.out.println("请您输入主人的密码：");
            String password=input.next();
            conn=getConnection();
            String sql="SELECT username from user where username=? and password=?";
            try {
                state=conn.prepareStatement(sql);
                state.setString(1, name);
                state.setString(2, password);
                rs=state.executeQuery();
                if(rs.next()){
                    System.out.println("---------恭喜您成功登录！---------");
                    System.out.println("----------您的基本信息---------");
                    conn=getConnection();
                    String sql2="SELECT id,username,money from user where username=?";
//					state=conn.prepareStatement(sql2);
//					state.setString(1, name);
//					rs=state.executeQuery();
                    rs=search(sql2,name);
                    if(rs.next()){
                        int uid=rs.getInt("id");
                        String uname=rs.getString("username");
                        Double uMoney=rs.getDouble("money");
                        System.out.print("登录成功!!!!");
                        System.out.println("姓名："+uname);
                        System.out.println("元宝数："+uMoney);
                        dealPet(uname,uid);
                    }
                }else{
                    System.out.println("登录失败，账户与密码不匹配");
                    login();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InputMismatchException e) {
            System.out.println("输入有误");
            login();
        }

        return false;
    }

    /**
     * 选择买宠物或者卖宠物的方法
     */
    public void dealPet(String ownerName,int uid) {

        System.out.println("您可以购买和卖出宠物，购买宠物请输入1，卖出宠物请输入2\n1.购买宠物\n2.卖出宠物\n3.返回上一级");
        try {
            Scanner input2=new Scanner(System.in);
            int choise2=input2.nextInt();
            if(choise2<1||choise2>3){
                System.out.println("输入有误");
                dealPet(ownerName,uid);
            }else{
                switch (choise2) {
                    case 1:
                        //购买宠物
                        buyPet(ownerName,uid);
                        break;
                    case 2:
                        //出售宠物
                        showSellPet(ownerName,uid);
                        break;
                    case 3:
                        //返回上一级
                        login();
                        break;
                    default:
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("输入有误");
            dealPet(ownerName,uid);
        }
    }
    /**
     * 显示主人拥有的宠物
     */
    public void showSellPet(String ownerName,int uid) {
        conn=getConnection();
        String sql25="SELECT pet.ID,pet.name from petowner,pet where petowner.ID=owner_id and petowner.ID="+uid+"";
        try {
            state=conn.prepareStatement(sql25);
            rs=state.executeQuery();
            System.out.println("以下是你拥有的宠物：");
//			//如果结果集为空，即该主人没有宠物，就返回上一级进行选择
//			if(!rs.next()){
//				System.out.println("您没有宠物，将自动返回上一级");
//				buyPet(ownerName, uid);
//			}
            while(rs.next()){
                int petid=rs.getInt("pet.ID");
                String petName=rs.getString("pet.name");
                System.out.println("这是"+petid+"号宠物，名字叫："+petName);
            }
            System.out.println("**************************************");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeObject1();
        sellPet(ownerName, uid);
    }

    public void sellPet(String ownerName,int uid) {
        System.out.println("请输入你想卖出的宠物序号：");
        try {
            Scanner input27=new Scanner(System.in);
            int choisePetId=input27.nextInt();
            System.out.println("请输入你要卖给的商店\n1.北京西苑\t2.重庆观音桥");
            int choiseStore=input27.nextInt();
            String sql30="SELECT pet.ID,pet.name from petowner,pet where petowner.ID=owner_id and petowner.ID="+uid+" and pet.ID="+choisePetId+"";
            Connection conn6=getConnection();
            try {
                state=conn6.prepareStatement(sql30);
                rs=state.executeQuery();
                if(rs.next()){
                    Connection conn9=getConnection();
                    conn9.setAutoCommit(false);
                    String sql40="UPDATE pet set owner_id=null,store_id="+choiseStore+" where pet.ID="+choisePetId+"";
                    state=conn9.prepareStatement(sql40);
                    int result20=state.executeUpdate();
                    String sql41="update petowner set money=money+5 where petowner.ID="+uid+"";
                    state=conn9.prepareStatement(sql41);
                    int result21=state.executeUpdate();
                    String sql42="update petstore set balance=balance-5 where petstore.ID="+choiseStore+"";
                    state=conn9.prepareStatement(sql42);
                    int result22=state.executeUpdate();
                    //获得当前时间
                    Long time1=System.currentTimeMillis();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    String dealTime=sdf.format(time1);
                    //将该条交易添加至交易账单中
                    String sql43="insert into account (deal_type,pet_id,seller_id,buyer_id,price,deal_time) VALUES (2,"+choisePetId+","+choiseStore+","+uid+",5,'"+dealTime+"')";
                    state=conn9.prepareStatement(sql43);
                    int result23=state.executeUpdate();

                    if(result20>0 && result21>0 && result22>0 & result23>0){
                        //提交事务
                        conn9.commit();
                        System.out.println("卖出成功");
                    }else{
                        //回滚事务
                        conn9.rollback();
                    }
                    dealPet(ownerName,uid);
                }else{
                    System.out.println("没有该宠物，卖出失败");
                    dealPet(ownerName,uid);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (InputMismatchException e) {
            System.out.println("输入错误，请重新输入");
            sellPet(ownerName, uid);
        }

    }

//	/**
//	 * 显示新培育宠物并且购买
//	 */
//	public void showNewPet() {
//		// TODO Auto-generated method stub
//
//	}

    /**
     * 宠物商店登录的方法
     * @return
     */
    public boolean petStoreLogin(){
        boolean flag=false;
        try {
            Scanner input=new Scanner(System.in);
            System.out.println("请先登录，请您先输入宠物商店的名字");
            String name=input.next();
            System.out.println("请您输入宠物商店的密码：");
            String password=input.next();
            conn=getConnection();
            String sq110="SELECT name,balance from petstore where name=? and password=?";
            state=conn.prepareStatement(sq110);
            rs=search(sq110, name,password);
            if(rs.next()){
                System.out.println("登录成功");
                PetStoreMake(name);
            }else{
                System.out.println("登录失败");
                login();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;
    }
    /*
     * 宠物商店培育新宠物
     */
    public void PetStoreMake(String storeName) throws SQLException {
        System.out.println("请输入数字进行选择：\n1.查询店内宠物\n2.培育新宠物\n3.退出登录");
        try {
            Scanner input=new Scanner(System.in);
            int choise7=input.nextInt();
            if(choise7<1||choise7>3){
                System.out.println("输入有误");
                PetStoreMake(storeName);
            }else{
                switch (choise7) {
                    case 1:
                        storePetQuery(storeName);
                        break;
                    case 2:
                        storeAddPet(storeName);
                        break;
                    case 3:
                        //退出登录，返回上一级
                        login();
                        break;

                    default:
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("输入有误");
            PetStoreMake(storeName);
        }
    }

    /**
     * 宠物商店培育新宠物的方法
     * @param storeName
     * @throws SQLException
     */
    public void storeAddPet(String storeName) throws SQLException {
        System.out.println("请输入你想添加的宠物的类型：");
        Scanner input=new Scanner(System.in);
        String typename=input.next();
        System.out.println("请输入该宠物的名字：");
        String petname=input.next();
        //查询该商店的ID
        String sql14="SELECT ID from petstore where name='"+storeName+"'";
        rs=search(sql14);
        int id=0;
        if(rs.next()){
            id=rs.getInt("ID");
        }
        conn=getConnection();
        String sql13="insert into pet (name,typename,health,love,birthday,store_id,neworold)VALUES(?,?,1,100,?,"+id+",'new')";
        //获取当前时间，作为宠物的生日
        Long time1=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String birth=sdf.format(time1);
        int a=update(sql13, petname,typename,birth);
        if(a>0){
            System.out.println("培育新宠物成功");
            PetStoreMake(storeName);
        }else{
            System.out.println("培育新宠物失败");
            PetStoreMake(storeName);
        }
    }

    /**
     * 在商店登录之后进行对店内的宠物进行查询
     * @param storeName
     */
    public void storePetQuery(String storeName) {
        System.out.println("正在查询店内宠物。。。");
        conn=getConnection();
        String sql11="SELECT pet.ID,pet.name,typename,birthday from pet,petstore where petstore.name=? and pet.store_id=petstore.ID";
        try {
            state=conn.prepareStatement(sql11);
            rs=search(sql11, storeName);
            int i=1;
            while(rs.next()){
                int id=rs.getInt("pet.ID");
                String name=rs.getString("pet.name");
                String typename=rs.getString("typename");
                String birthday=rs.getString("birthday");
                System.out.println("第"+i+"只宠物名字："+name+",宠物类型："+typename+",生日："+birthday);
                i++;
            }
            System.out.println("----------------------------");
            PetStoreMake(storeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *购买宠物的方法
     */
    public void buyPet(String ownerName,int uid){
        System.out.println("请输入选择购买范围，只输入选择项的序号");
        System.out.println("1:购买库存宠物\n2.购买新培育宠物\n3.返回上一级");
        try {
            Scanner input3=new Scanner(System.in);
            int choise5=input3.nextInt();
            if(choise5<1 || choise5>3){
                System.out.println("输入有误");
                buyPet(ownerName,uid);
            }else{
                switch (choise5) {
                    case 1:
                        showPetAll(ownerName,uid,2);
                        break;
                    case 2:
                        buyNewPet(ownerName,uid,2);
                        break;
                    case 3:
                        //返回上一级
                        dealPet(ownerName, uid);
                        break;

                    default:
                        break;
                }
            }


        } catch (InputMismatchException e) {
            System.out.println("输入有误");
            buyPet(ownerName,uid);

        }
    }

    public void buyNewPet(String ownerName,int uid,int petRoleType) {
        //用于判断查询是否有结果
        boolean havePet=false;

        System.out.println("正在帮你查询新宠物。。。。。");
        conn=getConnection();
        String sql31="SELECT id,pet_name from pet where pet.pet_role_type=2";
        try {
            state=conn.prepareStatement(sql31);
            rs=state.executeQuery();
            while(rs.next()){
                int petid=rs.getInt("id");
                String petName=rs.getString("pet_name");
                System.out.println("序号为："+petid+",名字为："+petName);
                havePet=true;
            }
            if(havePet){
                System.out.println("请输入你要购买的新宠物的序号：");
                try {
//					boolean havePet2=false;
                    Scanner input28=new Scanner(System.in);
                    int newPetId=input28.nextInt();
                    Connection conn7=getConnection();
                    String sql32="SELECT pet.ID,pet.name,pet.store_id from pet where pet.neworold='new' and pet.ID="+newPetId+"";
                    state=conn7.prepareStatement(sql32);
                    rs=state.executeQuery();
                    if(rs.next()){
                        int storeid=rs.getInt("pet.store_id");


                        Connection conn8=getConnection();
                        conn8.setAutoCommit(false);

                        String sql33="UPDATE pet set pet.neworold='old',pet.owner_id="+uid+",pet.store_id=null where pet.ID="+newPetId+"";
                        String sql34="update petowner set money=money-5 where petowner.ID="+uid+"";
                        String sql35="update petstore set balance=balance+5 where petstore.ID="+storeid+"";

                        //获得当前时间
                        Long time1=System.currentTimeMillis();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String dealTime=sdf.format(time1);
                        //将该条交易添加至交易账单中
                        String sql36="insert into account (deal_type,pet_id,seller_id,buyer_id,price,deal_time) VALUES (1,"+newPetId+","+storeid+","+uid+",5,'"+dealTime+"')";

                        state=conn8.prepareStatement(sql33);
                        int result13=state.executeUpdate();
                        state=conn8.prepareStatement(sql34);
                        int result14=state.executeUpdate();
                        state=conn8.prepareStatement(sql35);
                        int result15=state.executeUpdate();
                        state=conn8.prepareStatement(sql36);
                        int result16=state.executeUpdate();
                        if(result13>0 && result14>0 && result15>0 && result16>0){
                            //如果都成功执行，改变数据，那就提交事务
                            conn8.commit();
                            System.out.println("购买成功");
                        }else{
                            //如果中加你有一条没有执行成功那就回滚事务
                            conn8.rollback();
                        }
                        buyPet(ownerName, uid);
                    }else{
                        System.out.println("输入错误，没有该序号的新宠物");
                        buyNewPet(ownerName, uid,2);
                    }
                } catch (InputMismatchException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("暂时还没新宠物");
                buyPet(ownerName, uid);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    /**
     * 展示库存宠物名字，序号，类型的方法
     */
    public void showPetAll(String ownerName,int uid,int petRoleType) {
        System.out.println("---------以下是库存宠物--------");
        conn=getConnection();
        String sql6="SELECT pet.id,pet_name,pet_type,pet_price,owner_id,store_name from pet,pet_store where pet_store.id = pet.owner_id and pet.owner_type = 2";
        try {
            state=conn.prepareStatement(sql6);
            rs=state.executeQuery();
            while(rs.next()){
                int petId=rs.getInt("id");
                int storeId=rs.getInt("owner_id");
                String petName=rs.getString("pet_name");
                String storeName=rs.getString("store_name");
                String petType=rs.getString("pet_type");
                String money=rs.getString("pet_price");
                System.out.println("序号："+petId+"，我的名字叫:"+petName+"，我的品种是："+petType+",要购买我要花："+money+"个元宝!我属于："+storeName);
            }
            System.out.println("请输入你想购买的宠物编号：");
            try {
                Scanner input17=new Scanner(System.in);
                int choise6=input17.nextInt();
                //对在商店里的宠物进行ID查询，符合的就购买
                conn=getConnection();
                String sql15="SELECT pet.id,pet_name,pet_role_type,pet_type,pet_owner.id,store_name from pet,pet_store where pet.store_id=pet_owner.id and pet.owner_type = 2 and pet.id="+choise6;
                try {
                    state=conn.prepareStatement(sql15);
                    rs=state.executeQuery();
                    if(rs.next()){
                        //这里是宠物主人购买宠物的代码，将宠物的store_ID设置为null，将宠物的owner_ID设置为购买主人的ID
                        //然后主人账户减钱，商店的结余加钱,将该条交易添加至交易账单中
                        int store_id=rs.getInt("petstore.ID");//这里是选择的宠物所属商店的ID
                        //这里用创建一个新的连接
                        Connection conn1=getConnection();
                        //开启事务
                        conn1.setAutoCommit(false);
                        //将宠物的store_ID设置为null，将宠物的owner_ID设置为购买主人的ID
                        String sql18="update pet set owner_id=1,store_id=NULL where pet.ID="+choise6+"";
                        //宠物主人减钱
                        String sql19="update petowner set money=money-5 where petowner.ID="+uid+"";
                        //宠物商店加钱
                        String sql20="update petstore set balance=balance+5 where petstore.ID="+store_id+"";
                        //获得当前时间
                        Long time1=System.currentTimeMillis();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String dealTime=sdf.format(time1);
                        //将该条交易添加至交易账单中
                        String sql21="insert into account (deal_type,pet_id,seller_id,buyer_id,price,deal_time) VALUES (1,"+choise6+","+store_id+","+uid+",5,'"+dealTime+"')";

                        state=conn1.prepareStatement(sql18);
                        int result2=state.executeUpdate();
                        state=conn1.prepareStatement(sql19);
                        int result3=state.executeUpdate();
                        state=conn1.prepareStatement(sql20);
                        int result4=state.executeUpdate();
                        state=conn1.prepareStatement(sql21);
                        int result5=state.executeUpdate();
                        if(result2>0 && result3>0 && result4>0 && result5>0){
                            //如果都成功执行，改变数据，那就提交事务
                            conn1.commit();
                            System.out.println("购买成功");
                        }else{
                            //如果中加你有一条没有执行成功那就回滚事务
                            conn1.rollback();
                        }

                        //返回上一级
                        buyPet(ownerName,uid);
                    }else{
                        System.out.println("购买失败");
                        //返回上一级
                        buyPet(ownerName,uid);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (InputMismatchException e) {
                System.out.println("输入有误");
                showPetAll(ownerName,uid,1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}



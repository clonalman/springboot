﻿//本地生成SSH key  
ssh-keygen -t rsa -C "clonalman@hotmail.com"  
//登录github,添加"SSH Keys"  
本地：~/.ssh/id_rsa.pub  
远程：https://github.com/settings/ssh  

//本地项目文件夹生成一个.git文件  
git init  
//添加文件到仓库  
git add .  
//提交的文件注释说明，最好说明一下，否则有时候会出错  
git commit -m "知识图谱"  
//将本地仓库关联到GitHub上的仓库里去  
git remote add origin git@github.com:clonalman/springboot.git  
//首次提交要git pull 一下 
git pull origin master  
//将代码提交到GitHub上  
git push -u origin master  
//查看github上的仓库地址  
git remote -v  






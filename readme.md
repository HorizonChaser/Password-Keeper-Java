# JPassKeeper - A Simple Password Keeper Written in Java
## Current Status
![](https://img.shields.io/github/last-commit/HorizonChaser/Password-Keeper-Java) ![](https://img.shields.io/codeclimate/maintainability/HorizonChaser/Password-Keeper-Java) ![](https://img.shields.io/codeclimate/maintainability-percentage/HorizonChaser/Password-Keeper-Java)
[![CodeFactor](https://www.codefactor.io/repository/github/horizonchaser/password-keeper-java/badge)](https://www.codefactor.io/repository/github/horizonchaser/password-keeper-java)

![](https://img.shields.io/badge/Language-Java-orange.svg)  ![](https://img.shields.io/badge/Status-InDev%20(Beta)-yellow.svg)  ![](https://img.shields.io/badge/GUI-JavaFX-blue.svg)  ![](https://img.shields.io/github/license/HorizonChaser/Password-Keeper-Java)

InDev: In development, there would be changes in the project

Alpha: Most major functions have been implemented, minor ones would be implemented as soon as first refactor finished

Beta: All major and minor functions have been implemented, and we are testing the project.

## To-Do List
- [x] Default database detect and manually choose
- [x] Database import/export and integrity check 
- [x] Main UI implementation
- [x] Main UI function implementation - 100% done
- [ ] **Refactor and improve maintainability**
- [ ] "Open" button in MainUI

## A Brief Intro

JPassKeeper (abbr. JPK) is a simple password written in JavaFX. 

It supports import/export password database as a single file with AES encryption, and the key of the AES encryption is generated from hash of the username and password which used to log into JPK.

**Although it seems to be safe, but I CANNOT make any guarantee on safety, or anything like that.**

**JPK is just a toy model inspired by [JPass](https://github.com/gaborbata/jpass) . Again, the former statement is NOT any kind of guarantee either. So use JPK at your own risk.**

## Hash & Encryption Method

Login hash: sha-256( md5(\$name.\$pass.\$salt).\$salt ) 

Database encryption: AES-128-CBC (may support AES-256-CBC later)

Database key: sha-256( sha-256(\$pass.\$salt.\$name).\$salt )

Database verify: CRC-32


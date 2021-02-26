# JPassKeeper - A Simple Password Keeper Written in Java
## A Brief Intro

![](https://img.shields.io/github/last-commit/HorizonChaser/Password-Keeper-Java) ![](https://img.shields.io/codeclimate/maintainability/HorizonChaser/Password-Keeper-Java) ![](https://img.shields.io/codeclimate/maintainability-percentage/HorizonChaser/Password-Keeper-Java)

![](https://img.shields.io/badge/Language-Java-orange.svg)  ![](https://img.shields.io/badge/Status-InDev-yellow.svg)  ![](https://img.shields.io/badge/GUI-JavaFX-blue.svg)  ![](https://img.shields.io/github/license/HorizonChaser/Password-Keeper-Java)

JPassKeeper (abbr. JPK) is a simple password written in JavaFX. 

It supports import/export password database as a single file with AES encryption, and the key of the AES encryption is generated from hash of the username and password which used to log into JPK.

**Although it seems to be safe, but I CANNOT make any guarantee on safety, or anything like that.**

**JPK is just a toy model inspired by [JPass](https://github.com/gaborbata/jpass) . Again, the former statement is NOT any kind of guarantee either. So use JPK at your own risk.**

## Hash & Encryption Method

Login hash: sha-256 ( md5 (\$name.\$pass.\$salt).\$salt )

Database encryption: AES-256-CBC


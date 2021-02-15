a = "000"
while(a != str(chr(0x4))):
    a = input()
    a = bytes(a, encoding="UTF-8")

    for i in a:
        print(i, end=", ")
    print()

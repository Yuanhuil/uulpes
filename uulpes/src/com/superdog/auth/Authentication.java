package com.superdog.auth;

import SuperDog.Dog;
import SuperDog.DogApiVersion;
import SuperDog.DogStatus;

public class Authentication {

    public static final String vendorCode = new String(
            "VLmHONNUlQ3SzgpRX0MCZ9sTnzrg/a/vtTY/aFYlYJagBUMm3q1MXd317J4Av7C3+kfxi1rEvY/"
            + "tPDNBCQBhEwUzSxpX4dBsW8TCROdIJwmP1QmOgvUuw7f2MskbqyfwMUAsO2yEyIHDfNPmgFao3"
            + "5Gm7GDOPqGDz6Zul3+nQHKwdq6zprY4leuwpTDbU/CTOScdaSUoWhI0X60Eosyo9KDi3mHAt//W"
            + "1ri+UKwMFddD8fRQYpVKiCr9csfyVGNYOhUh6Iz1wK6vgNKnPjgAqcXfNJk5WDzlTrAAzBhCSLwx"
            + "5ACQMAculhwULLAseZPv8sPLYMykc4YAdAfmDJBy/bzBLIS0X06k9iu0qC66ez1Jxj4/ztUfUVNm"
            + "JVRdLw+Q5GB6dB/myg1UgW/LKQH2UcGOyPgU3jUAnxyepmLQIHSqNE/xv+BSaxGyDZYZmLShTXAE"
            + "+iu7sh/sypusMO4dUMZEbp4LtyZIpfaAZY/bLVHZbshRXnx+0mzIyZf0+exv1kU60FiaNnGvySQL"
            + "Dk8SM0UDoPwC73nxtcd3S2YqH+5+JrsSm4HdZiKHjYTUfqm0+rEz6enkmCvwWOrlbqNREwzcUHbS"
            + "Sw3myJ+dxAqMAq51CeP9s6P+uYyJLwN9ws2bYXogHlwGlCX36QXaMXnBUtB3QFuYiYKcJ3JyHKdl"
            + "jZB+yajU7g/AA1OCydjYV5m/HUr5b2HdJtiV16Ml6KTNgfFQscvTfVtavTChq90ck2vMqBY0Cz6K"
            + "QJHl8ZTk/p+fOOdYzC74caJPbw5/n2siZwG04c0/RdQ6xJiObauOcVLxEYiEbkzbKrfihdJKWB5W"
            + "Wzp2dfBXPBKaIOhqoGWZ2z6IxkR8RUVO1+JtQCtUGF14H27skgbplMBTWdebAuXzoO9Zlm5mv/jc"
            + "ztIYVqbuy6my14RyXzKza889u3puHu+aVnqJ8TB2zV3oI+TRiCSs1LCEJOBectnRXnWtBPQseA==");

    public static final int DEMO_MEMBUFFER_SIZE = 128;

    public static String NewgenChallenge() {
        int[] dll_status = { 0 };
        String s = null;
        s = getChallenge(dll_status);
        return s;
    }

    public static String getDog() {
        int status;
        int fsize;
        Dog curDog = new Dog(Dog.DOG_DEFAULT_FID);
        curDog.login(vendorCode);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("OK");
            break;
        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        case DogStatus.DOG_NOT_FOUND:
            System.out.println("SuperDog not found");
            return "0";
        case DogStatus.DOG_INV_VCODE:
            System.out.println("invalid vendor code");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        default:
            System.out.println("login to default feature failed");
        }

        DogApiVersion version = curDog.getVersion(vendorCode);
        status = version.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            break;
        case DogStatus.DOG_NO_API_DYLIB:
            System.out.println("SuperDog API dynamic library not found");
            return "2";
        case DogStatus.DOG_INV_API_DYLIB:
            System.out.println("SuperDog API dynamic library is corrupt");
            return "3";
        default:
            System.out.println("unexpected error");
        }

        fsize = curDog.getSize(Dog.DOG_FILEID_RW);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("file size is " + fsize + " bytes");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_INV_FILEID:
            System.out.println("invalid file id");
            break;
        case DogStatus.DOG_NOT_FOUND:
            System.out.println("SuperDog not found");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        default:
            System.out.println("could not retrieve data file size");
        }

        if (fsize != 0) { /* skip data file access if no data file available */

            /******************************************************************
             * dog_read read data file
             */

            /* limit file size to be used in this demo program */

            if (fsize > DEMO_MEMBUFFER_SIZE)
                fsize = DEMO_MEMBUFFER_SIZE;

            System.out.println("\nreading " + fsize + " bytes from data file   : ");

            byte[] membuffer = new byte[DEMO_MEMBUFFER_SIZE];

            curDog.read(Dog.DOG_FILEID_RW, 0, membuffer);
            status = curDog.getLastError();

            switch (status) {
            case DogStatus.DOG_STATUS_OK:
                System.out.println("OK");
                break;
            case DogStatus.DOG_INV_HND:
                System.out.println("handle not active");
                break;
            case DogStatus.DOG_INV_FILEID:
                System.out.println("invalid file id");
                break;
            case DogStatus.DOG_MEM_RANGE:
                System.out.println("beyond memory range of attached SuperDog");
                break;
            case DogStatus.DOG_NOT_FOUND:
                System.out.println("SuperDog not found");
                break;
            case DogStatus.DOG_LOCAL_COMM_ERR:
                System.out.println("communication error between API and local SuperDog License Manager");
                break;
            default:
                System.out.println("read data file failed\n");
            }
        }
        return null;
    }

    public native static String getChallenge(int status[]);

    public native static int verifyDigest(int ivendorid, int idogid, String strchlge, String strrspn, String strfctr);

}

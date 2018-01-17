
/****************************************************************************
*
* Demo program for SuperDog licensing API
*
*
* Copyright (C) 2014 SafeNet, Inc. All rights reserved.
*
*
* SuperDog DEMOMA key with features 42 in product 12 required
*
****************************************************************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;

import SuperDog.Dog;
import SuperDog.DogApiVersion;
import SuperDog.DogStatus;
import SuperDog.DogTime;

class dog_demo {
    public static final int DEMO_MEMBUFFER_SIZE = 128;

    public static final String vendorCode = new String(
            "SeNG1zqFHw1K06wLoBQAXRpp1yjxAFMFiQVKl6a6/SlXqSevMfh8kI0ELE8BBh8sXv7Ivpl1R05kn3d5QU8ejFkZkoTCqPRK/3FaIkCo3yUjOYO6BIyF2gAE/9ICK51N19gctLJYTqfFPd0+DftZ/d2YsYaO88NAiauOX2sco90F3i20QxVVoCk5JPY6CMlPqKSIdMBPjFnlyo82683fgi3tWanFty1w2EhbY15Xe+kYQFqGBAXF5xTKERDjq4xKdwuftdOtHWBbnrSGKzD4AZdlCjiz+x/yLa8igmanzzHE1zXMrprFB7cx+m4HGzXbrX2/wjOMFYGns+DouALvXEhpvEN66n2Q9jTi3E/4G85UumFMShowa06lWeQV296u2fSWg9+/oNM3idnC+4Kc+02DrJOxIu5RQfuxhvXbB/CAbdoa1CZGwaJowrXEd0abOen5iiGKMrY7yRQNlskqRUXBM9GMxhlGxuSzEU9qeI/FBf735r5+HcotiJbT7316/Jk8aV0qeoaNftDhVWflMvDZ+BFtqKDJX7tjlzDqQqwM00ZhroofkpEd/f19w9EkkbQiYxviJvreAWZZEUgLjeZBdw7O7GpULOcULboaqM/Lpc6r/p3NOE3KQKymJrkj0g+a5+M1rgRB2YstmVZnsLtjwLNDxv+25xh7DtVePd29pn00EBq6wMNL1NsBMxep6xgt2DzTNjUzv2n1xRkrok6Tlc37vqZNGrd3haacdDNm2qYdmyOerTgGgZkaYGHRm0JhbaktMpCSkz2AU5pFiAfYsBl2Dz41DN4/emLrQn9Tfin2EQhEYjY58h6pBzHfgIVTr74rrib4Ox50VEPvc8wDTj243whVyxMEC8g745I6NC9oHyQkT8LcDNPr/fjTdWFel77swgEf8j7tD5PL9ej7/J7pdHa8Yc+Bn1cK5JbEPRRaekVIJUi2PWMmPrwWOIoe6yZFQgKrtWOFAMVsew==");

    public static final String productScope = new String("<dogscope>\n" + " <product id=\"12\"/>\n" + "</dogscope>\n");

    public static final String scope = new String("<dogscope />\n");

    public static final String view = new String("<dogformat root=\"my_custom_scope\">\n" + "  <dog>\n"
            + "    <attribute name=\"id\" />\n" + "    <attribute name=\"type\" />\n" + "    <feature>\n"
            + "      <attribute name=\"id\" />\n" + "      <element name=\"license\" />\n" + "      <session>\n"
            + "        <element name=\"hostname\" />\n" + "        <element name=\"apiversion\" />\n"
            + "      </session>\n" + "    </feature>\n" + "  </dog>\n" + "</dogformat>\n");

    public static final byte[] data = { 0x74, 0x65, 0x73, 0x74, 0x20, 0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x20, 0x31,
            0x32, 0x33, 0x34 };
    public static final String strdata = "test string 1234";

    private static DogTime datetime;

    /************************************************************************
     * helper function: dumps a given block of data, in hex and ascii
     */

    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    private static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    public static void dump(byte[] data, String margin) {
        int i, j;
        byte b;
        byte[] s = new byte[16];
        byte hex[] = { 0 };
        String shex;
        String PrtString;

        if (data.length == 0)
            return;

        s[0] = 0;
        j = 0;
        for (i = 0; i < data.length; i++) {
            if (j == 0)
                System.out.print(margin);
            b = data[i];
            if ((b < 32) || (b > 127))
                s[j] = '.';
            else
                s[j] = b;
            if (j < 15)
                s[j + 1] = 0;
            hex[0] = b;
            shex = toHexString(hex);
            System.out.print(shex + " ");
            j++;
            if (((j & 3) == 0) && (j < 15))
                System.out.print("| ");
            PrtString = new String(s);
            if (j > 15) {
                System.out.println("[" + PrtString + "]");
                j = 0;
                s[0] = 0;
            }
        }
        if (j != 0) {
            while (j < 16) {
                System.out.print("   ");
                j++;
                if (((j & 3) == 0) && (j < 15))
                    System.out.print("| ");
            }
            PrtString = new String(s);
            System.out.println(" [" + PrtString + "]");
        }
    }

    public static void main(String argv[]) throws java.io.IOException {
        int status;
        String info;
        int i;
        int fsize;
        int input = 0;
        String strEncrypted;
        String strDecrypted;

        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);

        Dog curDog = new Dog(Dog.DOG_DEFAULT_FID);

        System.out.println("\nThis is a simple demo program for SuperDog licensing API\n");
        System.out.println("Copyright (C) SafeNet, Inc. All rights reserved.\n\n");

        DogApiVersion version = curDog.getVersion(vendorCode);
        status = version.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            break;
        case DogStatus.DOG_NO_API_DYLIB:
            System.out.println("SuperDog API dynamic library not found");
            return;
        case DogStatus.DOG_INV_API_DYLIB:
            System.out.println("SuperDog API dynamic library is corrupt");
            return;
        default:
            System.out.println("unexpected error");
        }

        System.out.println("API Version: " + version.majorVersion() + "." + version.minorVersion() + "."
                + version.buildNumber() + "\n");

        /**********************************************************************
         * dog_login establish a context for SuperDog
         */

        System.out.print("login to default feature         : ");

        /* login feature 0 */
        /* this default feature is available in any SuperDog */
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
            break;
        case DogStatus.DOG_INV_VCODE:
            System.out.println("invalid vendor code");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        default:
            System.out.println("login to default feature failed");
        }

        /********************************************************************
         * dog_get_sessioninfo retrieve SuperDog attributes
         */

        System.out.print("\nget session info                 : ");

        info = curDog.getSessionInfo(Dog.DOG_KEYINFO);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.print("OK, SuperDog attributes retrieved\n\n" + "SuperDog info:\n===============\n" + info
                    + "\n===============\n");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_INV_FORMAT:
            System.out.println("unrecognized format");
            break;
        case DogStatus.DOG_NOT_FOUND:
            System.out.println("SuperDog not found");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        default:
            System.out.println("dog_get_sessioninfo failed");
        }

        /*******************************************************************/

        System.out.println("\npress ENTER to continue");
        input = in.read();

        /********************************************************************
         * dog_get_size retrieve the data file size of SuperDog
         */

        System.out.println("\nretrieving the data file size : ");

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
                dump(membuffer, "    ");
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

            /******************************************************************
             * dog_write write to data file
             */

            System.out.println("\nincrementing every byte in data file buffer");
            for (i = 0; i < fsize; i++)
                membuffer[i]++;

            System.out.println("\nwriting " + fsize + " bytes to data file     : ");

            curDog.write(Dog.DOG_FILEID_RW, 0, membuffer);
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
                System.out.println("write data file failed");
            }

            /******************************************************************
             * dog_read read data file
             */

            System.out.println("\nreading " + fsize + " bytes from data file   : ");

            curDog.read(Dog.DOG_FILEID_RW, 0, membuffer);

            switch (status) {
            case DogStatus.DOG_STATUS_OK:
                System.out.println("OK");
                dump(membuffer, "    ");
                break;
            case DogStatus.DOG_INV_HND:
                System.out.println("handle not active\n");
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
                System.out.println("read data file failed");
            }
        } /* end of data file demo */

        /**********************************************************************
         * dog_encrypt encrypts a block of data using SuperDog (minimum buffer
         * size is 16 bytes)
         */

        System.out.println("\nencrypting a data buffer:");
        dump(data, "     ");

        curDog.encrypt(data);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("encryption ok:");
            dump(data, "     ");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_TOO_SHORT:
            System.out.println("data length too short");
            break;

        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        default:
            System.out.println("encryption failed");
        }

        /**********************************************************************
         * dog_decrypt decrypts a block of data using SuperDog (minimum buffer
         * size is 16 bytes)
         */

        curDog.decrypt(data);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("decryption ok:");
            dump(data, "     ");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_TOO_SHORT:
            System.out.println("data length too short");
            break;

        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        default:
            System.out.println("decryption failed");
        }

        /**********************************************************************
         * encrypts a string using SuperDog
         */

        System.out.println("\nencrypting a string:");
        System.out.println(strdata);

        strEncrypted = curDog.encryptString(strdata);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("encryption ok:");
            System.out.println(strEncrypted);
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_TOO_SHORT:
            System.out.println("data length too short");
            break;

        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        default:
            System.out.println("encryption failed");
        }

        /**********************************************************************
         * decrypts a string using SuperDog
         */

        strDecrypted = curDog.decryptString(strEncrypted);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("decryption ok:");
            System.out.println(strDecrypted);
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_TOO_SHORT:
            System.out.println("data length too short");
            break;

        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        default:
            System.out.println("decryption failed");
        }

        /**********************************************************************
         * dog_get_time read current time from SuperDog
         */

        System.out.print("\nreading current time and date    : ");

        datetime = curDog.getTime();
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println(
                    "time: " + datetime.getHour() + ":" + datetime.getMinute() + ":" + datetime.getSecond() + " H:M:S");
            System.out.println("                                   date: " + datetime.getDay() + "/"
                    + datetime.getMonth() + "/" + datetime.getYear() + " D/M/Y");
            break;
        case DogStatus.DOG_INV_TIME:
            System.out.println("time value outside supported range\n");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        default:
            System.out.println("could not read time from SuperDog");
        }

        /**********************************************************************/

        System.out.print("login to feature 42              : ");

        Dog nextDog = new Dog(42);

        nextDog.login(vendorCode);
        status = nextDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("OK");
            break;
        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog found with feature 42 enabled");
            break;
        case DogStatus.DOG_NOT_FOUND:
            System.out.println("SuperDog not found");
            break;
        case DogStatus.DOG_INV_VCODE:
            System.out.println("invalid vendor code");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        default:
            System.out.println("login feature 42 failed");
        }

        System.out.println("\nencrypt/decrypt again to see different encryption for different features:");

        /**********************************************************************
         * dog_encrypt encrypts a block of data using SuperDog (minimum buffer
         * size is 16 bytes)
         */

        System.out.println("\nencrypting a data buffer:");
        dump(data, "     ");

        nextDog.encrypt(data);
        status = nextDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("encryption ok:");
            dump(data, "     ");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_TOO_SHORT:
            System.out.println("data length too short");
            break;

        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        default:
            System.out.println("encryption failed");
        }

        /**********************************************************************
         * dog_decrypt decrypts a block of data using SuperDog (minimum buffer
         * size is 16 bytes)
         */

        nextDog.decrypt(data);
        status = nextDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("decryption ok:");
            dump(data, "     ");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("handle not active");
            break;
        case DogStatus.DOG_TOO_SHORT:
            System.out.println("data length too short");
            break;

        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("no SuperDog DEMOMA key found");
            break;
        default:
            System.out.println("decryption failed");
        }

        nextDog.logout();
        status = nextDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("OK");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("failed: handle not active");
            break;
        default:
            System.out.println("failed with status " + status);
        }

        /**********************************************************************
         * dog_logout closes established session and releases allocated memory
         */

        System.out.print("\nlogout from default feature        : ");

        curDog.logout();
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("OK");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("failed: handle not active");
            break;
        default:
            System.out.println("failed");
        }

        /********************************************************************
         * dog_login_scope establishes a context for SuperDog allows
         * specification of several restrictions
         */

        curDog = new Dog(42);

        System.out.println("login to feature in specified product:");

        System.out.println(productScope);

        System.out.print("dog_login_scope                 : ");

        curDog.loginScope(productScope, vendorCode);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("OK");
            break;
        case DogStatus.DOG_FEATURE_NOT_FOUND:
            System.out.println("login to default feature failed");
            break;
        case DogStatus.DOG_NOT_FOUND:
            System.out.println("SuperDog not found");
            break;
        case DogStatus.DOG_INV_VCODE:
            System.out.println("invalid vendor code");
            break;
        case DogStatus.DOG_INV_SCOPE:
            System.out.println("invalid XML scope");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        default:
            System.out.println("login to feature failed with status " + status);
        }

        /*******************************************************************/

        System.out.print("\ngetting information about connected SuperDog:  ");

        info = curDog.getInfo(scope, view, vendorCode);
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            /* use the info you received ... */
            System.out.println("OK\n" + info);
            break;
        case DogStatus.DOG_INV_FORMAT:
            System.out.println("invalid XML info format\n");
            break;
        case DogStatus.DOG_INV_SCOPE:
            System.out.println("invalid XML scope\n");
            break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
            System.out.println("communication error between API and local SuperDog License Manager");
            break;
        case DogStatus.DOG_SCOPE_RESULTS_EMPTY:
            System.out.println("unable to locate a Feature matching the scope");
            break;
        default:
            System.out.println("dog_get_info failed with status " + status);
        }

        /********************************************************************
         * dog_logout closes established session and releases allocated memory
         */

        System.out.print("\nlogout                           : ");
        curDog.logout();
        status = curDog.getLastError();

        switch (status) {
        case DogStatus.DOG_STATUS_OK:
            System.out.println("OK");
            break;
        case DogStatus.DOG_INV_HND:
            System.out.println("failed: handle not active");
            break;
        default:
            System.out.println("failed\n");
        }
    }
}

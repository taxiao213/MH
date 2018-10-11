package com.haxi.mh.utils.dense;

/**
 * RSA密钥
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class KeyUtil {

    //服务端的RSA公钥(Base64编码)
    public final static String SERVER_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4ctl" +
            "7Pb7mKt3nuB6AFnoNcu2eL\n" +
            "FoU3YPefwjKzwUgmxcHQlCxSFeMyIiUSAFoRqB2sfpHTI+XeOLchlMcc30dLU5492miQ/414nhwJvashIVgSGnQ\n" +
            "SojAAmp12Ll0J/Mudsb3oMFnUTmR4F3kFqqgVP/xUJwjmgm4WHvOS64G3rJ+yFesFshOgUXpXb8LCAn4JbvBIyX\n" +
            "u8c+A/KW0BFzRu0OKjmoZ7vP5s1+ZY5MWvtDG9/dXqomatDQhokf1gp+UCQi9\n" +
            "UnTjh0kNODbZycXzSGuKcG6AiSlMpzrZHPv/2V0WsLkNkrc/qbY04HBuEh7frxYNLjMPaaXt9RxB2kpeQIDAQAB";

    //服务端的RSA私钥(Base64编码)
    public final static String SERVER_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIB" +
            "AQDhy2Xs9vuYq3ee4HoAWe\n" +
            "g1y7Z4sWhTdg95/CMrPBSCbFwdCULFIV4zIiJRIAWhGoHax+kdMj5d44tyGUxxzfR0tTnj3aaJD/jXieHAm9qyEh\n" +
            "WBIadBKiMACanXYuXQn8y52xvegwWdROZHgXeQWqqBU//FQnCOaCbhYe85Lrgbesn7IV6wWyE6BReldvwsICfglu\n" +
            "8EjJe7xz4D8pbQEXNG7Q4qOahnu8/mzX5ljkxa+0Mb391eqiZq0NCGiR/WCn5QJCL1SdOOHSQ04NtnJxfNIa4pwb\n" +
            "oCJKUynOtkc+//ZXRawuQ2Stz+ptjTgcG4SHt+vFg0uMw9ppe31HEHaSl5AgMBAAECggEANhLu+vv/LwXJZPwfW5\n" +
            "wsd02tCmR6NpqshCs1BPKjesSWnsGMAwfFtnszOrhvgLCHbqy3kC1rMDMBPzb9zrdjFtwb3A1GB8cSchPsQ6NPkZ\n" +
            "uxGLbFhl6f+S9XRGTD4t2SOuRVXUmFe693cqJ3ztNXyBckxsntiL06qnk4wDoHD1U6hNv2ZpybQ05QNuGjVUZDnZ\n" +
            "sorgHrZ6ATra8uHSlca6lJ3VsWMA65GUg2vr3dnkziPIqm5qZRVtMH2VB7gsG+H8kX0VdfJDhz54cO5FZvQnIusT\n" +
            "U8cojdEZXsgYcOXXGdsJds4VR+2Gn7t79hDeDqS+7uQxdPTUCf5bv7K7xYTQKBgQDz50PNlOdUvoPfeySat+4Ck3\n" +
            "R0cnzMphas7Z4dH4fPQcEF/LrU44O9BXNKJI3RFQaLpt4+rAN7C+tJl/BjZM8umV+p020Xa5qbpg+4kT7R7B7ftP\n" +
            "rxbsm2HQJdLnbvb0FjJasrmwkN7szRRfGoTugFKA9mKeb5xiyIQwL7KU/H3wKBgQDs/jaJ5524Snskq6t72K2BzX\n" +
            "0yDKlBjvQfJJq2p35gbWdQRx7kjx2+0E1mS+uU1Gx2c3mUXj0KP8DEgAknY5/OEOoXt2GlD74dfkc8lYXTp80kKe\n" +
            "kXS77DZXp8mqJEKeeAqBUzmEhICqKNy9pNCBD569oCZtpUfP24QJOHhUcZpwKBgD1uw6I1Xir3LYoNn0OlDjrx3w\n" +
            "QxjgOR1eZKl2eoeHGYk0CpznkusRDcHZTQLjQ6goKMTjAk7V+Bugq4TWELlC+di/3fdfvPK4sGwbDwTI1dVs1r7U\n" +
            "UAbagEGINV37khIawSYdHC4/3xZv9TCbIl5cTeuTo3VakV5EOR9V9DoYerAoGAQDdJ0+g8plkYMQGLP82ghhGGIO\n" +
            "+pVShFIcMrYN7VsY8zdV8wEcJtzuxmbJeZyAQfN6FmijtW0tYfh58h29h0zsNqiu6XuMAhRaCX8OSbFJTdUzVAAZ\n" +
            "32nL98hxLncQzhu8zPoeFbhJnNHhrklgbfgrjTo972vfn4TdrBnvEQKPUCgYBaRIFsovQUDCX5kx9jFnZ1i5kTF1\n" +
            "V17zHBqn3OhJJ8erzu5t+XqCle0ngIjxVrItQNLA+NAZBWkdE94DZXTNygD74N2eOuVhvIiSUIocXU\n" +
            "Jk2FDqkbn/p0jeTvN7zJzlasY0icQy572Simxe+fy3OamlH1ODYUmKB4Sc0tJEETIg==";

    //APP端的RSA公钥(Base64编码)
    public final static String APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0D4HCd8mI" +
            "NdUYfsgdynKRmb3Z5AKxUGiH3jl7bqG8WcabSImGKkwAY+KqW9iVAtpJUuBAnfy2kF8mwwptlUfPyAso0YNI/VjX" +
            "cVXyiFcLBJse4oFO589ImB7cKmnxNsTOhMtoTItxy6jAeEf3GBWDGIFmqxEVAhmXOF0UIH1TNhUsWp1RFn75CnR1" +
            "jwl+cPz5v9IWYoitxsh09TL9ePQi1DYh+3CHxgmnfJHLpl+ceWcxHN+BFwS16m2wyFlTKISNwLT0Oj+SO551HIBS" +
            "mKaKQqdRKFdsR+JDXudhY0vuSC7aLjGtKLXn2gnZS8Oduv15mM5aTdOu2XvoEw+quHRHQIDAQAB";

    //APP端的RSA私钥(Base64编码)
    public final static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQ" +
            "PgcJ3yYg11Rh+yB3KcpGZvdnkArFQaIfeOXtuobxZxptIiYYqTABj4qpb2JUC2klS4ECd/LaQXybDCm2VR8/ICyj" +
            "Rg0j9WNdxVfKIVwsEmx7igU7nz0iYHtwqafE2xM6Ey2hMi3HLqMB4R/cYFYMYgWarERUCGZc4XRQgfVM2FSxanVE" +
            "WfvkKdHWPCX5w/Pm/0hZiiK3GyHT1Mv149CLUNiH7cIfGCad8kcumX5x5ZzEc34EXBLXqbbDIWVMohI3AtPQ6P5I" +
            "7nnUcgFKYpopCp1EoV2xH4kNe52FjS+5ILtouMa0otefaCdlLw526/XmYzlpN067Ze+gTD6q4dEdAgMBAAECggEA" +
            "Ow58PIZ2b7XhjnADwwes/85Pt6UYCN0WN0q4GnVds7SDwBOBLlk87hRhRK/7+M3LofIJwkN2MgOEbQxEABuf4u0i" +
            "+6LwMxRye7NttDtFwpHvBhIcbc45LjiiiU7z71YZ28x4klCCvwww2mWX/Y2smZJq+HSGZDaFoS2nKPXSNVpZav+7" +
            "I2uUbsnuXkOgeptyG30hePveerC1WOHGZ11vNKtrweikFwnaddV1l5/JSpD46J9YXhvPjnLED4epzqvPXnhm/zSs" +
            "btJLZ7bELk+PLvMXtRGUHto4NUdqhoqH5zOfYxyC/FE223NPROFlODMCUj0/WEZyZmYb2zR7Bw6gmQKBgQD0054T" +
            "FCgHj7eo1Ha/CK+iR+0on8vbCDKcmU+QLx67p2eLN2Woq3i2dfuXTAPXpkn3UvxoozrTGc5Ba/VZmdZk4H0jYZK4" +
            "VR5C2uYHvjJmOanII2yqHBoat/Pg/vJbM7yN/DbBs5EY8IqR542ARi6ncHeJ+NkBYahMKb1Fl+AcMwKBgQDZvvv7" +
            "41rdWZ6wUXBaNiRsoflKUAZMACE9jirG51xkjnpAbnhswpM2wE2Ug/CEvduslWlLyyFySbZKJEuf2EsOhajKzaoi" +
            "F6SMdHY9vZy/WLdu5fX1jX776IB+fTlcTpP98v8qwrLh2isJtiZOVzFbgEHG/6yHMFt0jxlJTJ0NbwKBgGxec1z8" +
            "kg9uxRvdF8bNgHizn71YpjKIuWZp0jb/NHpZylJsLfpwFBmntbqtshG1kI4CVKm4fuRMSujYcZy8pJK4b4vGt9wS" +
            "CTgMjr+kp8hqA8YMMCBIGiHzhOb10q8eAntSxaYN5s8EQkH9HbxG1IXXGYKf/OZIpOc5nob8h/3TAoGBALE/iheN" +
            "BxZQMaENagvFLvlPEIKxia6sHGLjK2cmRkuEv3dbdaPy76sN9xkCHfOwKPXf2grcm6lQ35aS3QyNIOv9WV1c9TUz" +
            "UbsTwnyXsNUB/q/Deegc+lVM+ev9O4gsXsPF3niMbNj0ozwQjjYPPBpj2SeIUITrlEbZhZfpSZ75AoGAD9HMqKCj" +
            "GkgouC1DjBHpDCzhNCqOAx788Diop1ZbG37f/nQZY7zJNYUK3KhkJyvNE/gkRHmFUlQ3YRKNBWw5vF/WwLBvUxSG" +
            "5Jxx0FMhe21ilNZFuUIu/BV4/zYSJGJdJfSKuisr//V44gFb9HU7BKkKTqXBlT31XGamx1TuBac=";

    public static final String CONTENT = "{\n" +
            "    \"ak\": \"qOPxtsB1WaxZsaPSEIO13tgQ4qYoFEcWqliMfKsn/kDGHFCeBmZaGuL91Cjhm0F6Y/OOwpb5Fr5kl0DcvMiqOzKSABrpS2wKxa1P4yD+S1uyW3222X7gHUEOUbCuPrVLqgemphp8QsIQg5qQMDWlFPxZce3AtjNpKOFxkPmK7qTh3RNeFuEiGvKIuWAFm0tzHAqefIR8Nxs9K0kS958hoBo9rxmpPv4uqqB2Mgb5L68hUJNKVnY9o3dL8lWcKPEND0oN4lmQnWWyOc0jW2KKiQBxLLo6NHFq1nI+55YPGZ8deNcax//W891RB1c2lPZ0ie2rNDRkf9rrtMlhRKF/TA==\",\n" +
            "    \"ct\": \"GCiH9NoMFmX5W8geF5Hpzg==\"\n" +
            "}";
}

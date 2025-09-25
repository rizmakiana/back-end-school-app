# Staff API Specs

## Register Staff
Endpoint: POST /api/staff

Request Header: 
- Accept-Language: en

Request Body: 
```json
{
    "name": "Zahra Hanifa Mafriandi",
    "gender": "FEMALE",
    "birtplaceId": "3210",
    "birtdate": "14-04-2003",
    "username": "zahrahanifamafriandi",
    "email": "zahra@gmail.com",
    "phoneNumber": "08319812311",
    "password": "rahasia",
    "confirmPassword": "rahasia",
    "addressPlaceId": "321010",
    "address": "Jl. Perjuangan VI No. 8",
}
```

Response Body(Success):
```json
{
    "message": "Registered account succesfully"
}
```

Response Body(Failed: Validation):
```json
{
    "errors": {
        "name": "Name can't be blank",
        "gender": "Gender can't be blank",
        "birtplaceId": "Birthplace can't be blank",
        "birtdate": "Date can't be blank",
        "birthmonth": "Month can't be blank",
        "birtyear": "Year can't be blank",
        "username": "Username can't be blank",
        "email": [
            1: "Invalid e-mail address",
            2: "Email can't be blank"
        ],
        "phoneNumber": [
            1: "Invalid phone number. must 08 at first",
            2: "Phone number can't be blank"
        ],
        "password": "Password can't be blank",
        "confirmPassword": "Confirm Password can't be blank",
        "addressPlaceId": "District address can't be blank",
        "address": "Address can't be blank",
    }
}

```
Response Body(Failed: back-end):
```json
{
    "errors": {
        "username": "Username already exists",
        "email": "E-mail already exists",
        "phoneNumber": "Phone number already exists",
        "birthdate": "Invalid birthdate",
        "password": "Password not equals",
        "address": "Address not found"
    }
}
```

## GET Staff
Endpoint: POST /api/staff

Request-Header:
- Accept-Languange
- Authorization: Bearer Token

Response Body(Success):
```json
{
    "name": "Zahra Hanifa Mafriandi",
    "birtplaceId": "3210",
    "birthdate": "14",
    "birthmonth": "04",
    "birthyear": "2003",
    "username": "zahra",
    "email": "email",
    "phoneNumber": "08319073141",
    "addressPlaceId": "321010",
    "address": "Jl. Perjuangan IV No 10"
}
```

Response Body(Failed: Unathorized)
```json
{
    "errors": "Unathorized"
}
```

## UPDATE Staff

Endpoint: PUT /api/staff

Request-Header:
- Accept-Language: 
- Authorization: Bearer Token

Request Body:
```json
{
    "name": "Zahra Hanifa",
    "gender": "FEMALE",
    "birtplaceId": "3210",
    "birtdate": "14",
    "birthmonth": "04",
    "birthyear": "2003",
    "username": "zahra",
    "email": "zahra@gmail.com",
    "phoneNumber": "0883213413",
    "password": "", // fill if want to change password
    "newPassword": "", // fill if want to change password
    "confirmPassword": "", // fill if want to change password
    "addressPlaceId": "321010",
    "address": "Jl. Perjuangan IV no 96",
}
```

Response Body(Sucsess): 
```json
{
    "message": "Updated account succesfully"
}
```

Response Body(Failed: Validation):
```json
{
    "errors": {
        "errors": {
        "name": "Name can't be blank",
        "gender": "Gender can't be blank",
        "birtplaceId": "Birthplace can't be blank",
        "birtdate": "Date can't be blank",
        "birthmonth": "Month can't be blank",
        "birtyear": "Year can't be blank",
        "username": "Username can't be blank",
        "email": [
            1: "Invalid e-mail address",
            2: "Email can't be blank"
        ],
        "phoneNumber": [
            1: "Invalid phone number. must 08 at first",
            2: "Phone number can't be blank"
        ],
        "password": "Password can't be blank",
        "confirmPassword": "Confirm Password can't be blank",
        "addressPlaceId": "District address can't be blank",
        "address": "Address can't be blank",
    }
    }
}
```



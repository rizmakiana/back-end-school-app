# Department API Specs

## Create 
Endpoint: POST /api/staff/departments

Request-Header: 
- Accept-Language: 
- Authorization Bearer: Token

Request Body: 
```json
{
    "name": "Ilmu Pengetahuan Alam",
    "code": "IPA"
}
```

Response Body(Success):
```json
{
    "message": "Department added succesfully"
}
```
Response Body(Fail: Validation):
```json
{
    "name": "Department name cannot be empty",
    "code": "Code name cannot be empty"
}

```
Response Body(Fail: back-end):
```json
{
    "name": "Department name already exists",
    "code": "Code name already exists"
}
```

## Get All

Endpoint: GET /api/staff/departments
Request-Header: 
- Accept-Language: 
- Authorization Bearer: Token

Response Body(Success): 
```json
{
    "data": 
    [
        "name": "Ilmu Pengetahuan Alam",
        "code": "IPA"
    ],
    [
        "name": "Ilmu Pengetahuan Sosial",
        "code": "IPS"
    ]
}
```
## Get

Endpoint: GET /api/staff/departments/{id}
Request-Header: 
- Accept-Language: 
- Authorization Bearer: Token

Response Body(Success): 
```json
{
    "data": 
    {
        "name": "Ilmu Pengetahuan Alam",
        "code": "IPA"
    }
    
}
```

## Update

Endpoint: PATCH /api/staff/departments/{id}

Request-Header: 
- Accept-Language: 
- Authorization Bearer: Token

Request Body: 
```json
{
    "name": "Materi Ilmu Pengetahuan Alam",
    "code": "MIPA"
}
```
Response Body(Success): 
```json
{
    "message": "Department updated succesfully"
}
```

Response Body(Fail: Validation):
```json
{
    "name": "Department name cannot be empty",
    "code": "Code name cannot be empty"
}
```

## Delete

Endpoint: DELETE /api/staff/departments/{id}

Request Header:
- Accept-Language: en
- Authorization Bearer: Token

Response Body(Success): 
```json
{
    "message": "Department deleted succesfully"
}
```

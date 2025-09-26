# 📌 Region API Specs

## Province

### GET Provinces List

Endpoint: GET /api/region/provinces

```json
{
    "data": {
        {
            "id": "32",
            "name": "Jawa Barat"
        },
        {
            "id": "33",
            "name": "Jawa Tengah"
        }
    }
}
```

### GET Province

Endpoint: GET /api/region/provinces/{id}
- Path variable: id

```json
{
    "data": {
        "id": "32",
        "name": "Jawa Barat"
    }
}
```

## Regency

### GET Regencies List by Province Id
Endpoint: GET /api/region/regencies?provinceId={id}
- Query param: {id}
```json
{
    "data": {
        {
            "id": "3201",
            "name": "Kab. Bogor"
        },
        {
            "id": "3202",
            "name": "Kota Bogor"
        }
    }
}
```
### GET Regency
Endpoint: GET /api/region/regencies/{id}
- Path Varible: {id}
```json
{
    "data": {
        {
            "id": "3201",
            "name": "Kab. Bogor"
        },
        {
            "id": "3202",
            "name": "Kota Bogor"
        }
    }
}
```

## District

### GET Districts List by Regency Id
Endpoint: GET /api/region/districts?regencyId={id}
- Query param: {id}
```json
{
    "data": {
        "id": "320107",
        "name": "Cileungsi"
    },
    {
        "id": "320108",
        "name": "Gunung Putri"
    }
}
```

### GET District
Endpoint: GET /api/region/districts/{id}
- Path variable: {id}
```json
{
    "data": {
        "id": "320107",
        "name": "Cileungsi"
    }
}
```

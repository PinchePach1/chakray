#User API

**Author:** Diego Pacheco
**Project:** User API
**Date:** 06/09/2025

---

## **1. Descripción**

Esta API permite gestionar usuarios, incluyendo:

    - Generación de usuarios con nombre, correo, telefono, RFC, contraseñas y direcciones
    - Listado de usuarios
    - Actualización de usuarios
    - Validaciones de RFC y teléfono
    - Documentación Swagger/OpenAPI incluida
    - Pruebas con Postman

Se incluye Dockerfile para empaquetar y correr la API desde esta tecnologia.

---

## **2. Requisitos**

    - Java 17 (OpenJDK o Eclipse Temurin)
    - Maven 3.x
    - Docker (opcional, si se desea correr la API en contenedor)
    - Git (opcional, para clonar repo)
    - Postman (para importar colección y testear endpoints)

---

## **3. Estructura del proyecto**

    Diego_Pacheco.zip
    │
    ├─ src/ # Código fuente Java
    ├─ target/ # Carpeta generada por Maven (JAR compilado)
    ├─ Dockerfile # Para crear la imagen Docker
    ├─ pom.xml # Dependencias y build
    ├─ postman_collection.json # Colección para Postman
    ├─ swagger.yaml # Documentación OpenAPI
    └─ README.md # Este archivo

---

## **4. Instalación y ejecución**

    1. Descomprimir `FirstName_LastName.zip`
    2. Abrir terminal en la carpeta raíz del proyecto
    3. Compilar el proyecto:

    ```bash
    mvn clean package
    mvn spring-boot:run

    API disponible en http://localhost:8080
    ```

---

## **5. Documentación y prueba**

    Swagger/OpenAPI --->  http://localhost:8080/swagger-ui/index.html

    Postman ---> Contiene todas las operaciones CRUD y ejemplos de payload https://fx-solutions-4971.postman.co/workspace/FX-Solutions-Workspace~d9e09902-e59e-4983-836b-0fef89ec856b/collection/41076585-f6a34c82-47ca-4c4a-b2e1-a7cb5eea1542?action=share&creator=41076585

---

## **6. Extras**

    Para clonar el proyecto desde Git

    git clone https://github.com/PinchePach1/chakray.git

---

## **7. Contacto**

    Para dudas o soporte:
        depachvi@gmail.com
        5548462904
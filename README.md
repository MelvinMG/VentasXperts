
# Bienvenido al repositorio 

Este repositorio contiene el backend en **Django REST Framework** y el frontend en **React** para el proyecto VentasXperts. Aquí encontrarás los comandos e instrucciones necesarias para instalar y ejecutar el proyecto correctamente.

## 🚀 Tecnologías utilizadas

![Django](https://img.shields.io/badge/Django-092E20?style=for-the-badge&logo=django&logoColor=white) ![DjangoREST](https://img.shields.io/badge/Django%20REST-ff1709?style=for-the-badge&logo=django&logoColor=white) ![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white) ![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)

----------

## 📌 Requisitos

Asegúrate de tener instalado lo siguiente antes de comenzar:

-   **Python 3.x**
-   **pip** (administrador de paquetes de Python)

## ⚙️ Pasos para configurar el proyecto

Sigue estos pasos para configurar y ejecutar el proyecto localmente.

### 1️⃣ Clonar el repositorio

```bash
# Clona el repositorio desde GitHub
git clone https://github.com/MelvinMG/VentasXperts.git
cd VentasXperts

```

### 2️⃣ Crear el entorno virtual

```bash
python -m venv .venv
# o tambien se puede usar el virtualenv
virtualenv .venv
# Activar el entorno virtual en Windows
.venv\Scripts\activate
# En MacOS/Linux
source venv/bin/activate

```

### 3️⃣ Instalar las dependencias

```bash
pip install -r requirements.txt

```

> ⚠️ Si falla la instalación, intenta instalar las dependencias manualmente.

### 4️⃣ Aplicar las migraciones

```bash
python manage.py makemigrations
python manage.py migrate

```

> ❗ **Nota:** Antes de ejecutar estos comandos, borra el contenido de la carpeta `migrations` si es necesario.

### 5️⃣ Crear un superusuario

```bash
python manage.py createsuperuser

```

Sigue las instrucciones en pantalla y usa:

-   **Usuario:** `SU`
-   **Correo:** `tu_correo@example.com`
-   **Contraseña:** `SU1234`

### 6️⃣ Ejecutar el servidor de desarrollo

```bash
python manage.py runserver

```


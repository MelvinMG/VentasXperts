from django.apps import AppConfig

class AdministracionConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'Administracion'

    def ready(self):
        # Importar el archivo de señales para que la señal se registre correctamente
        import Administracion.signals  # Asegúrate de usar la ruta correcta

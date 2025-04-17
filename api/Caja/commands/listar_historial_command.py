# commands/listar_historial_command.py
from rest_framework import status
from rest_framework.response import Response
import os
import datetime
from django.conf import settings
from .ticket_command import TicketCommand

class ListarHistorialCommand(TicketCommand):
    def execute(self, request, *args, **kwargs):
        pdf_folder = os.path.join(settings.BASE_DIR, 'Caja', 'media', 'pdf_ticket')

        if not os.path.exists(pdf_folder):
            return Response({'message': 'No existe la carpeta'}, status=status.HTTP_404_NOT_FOUND)

        pdf_files = []
        for file_name in os.listdir(pdf_folder):
            if file_name.endswith('.pdf'):
                file_path = os.path.join(pdf_folder, file_name)
                creation_time = datetime.datetime.fromtimestamp(os.path.getctime(file_path))
                pdf_files.append({
                    'name': file_name,
                    'creation_time': creation_time.strftime("%Y-%m-%d %H:%M:%S"),
                    'path': f'{settings.MEDIA_URL}pdf_ticket/{file_name}'
                })

        pdf_files.sort(key=lambda x: x['creation_time'], reverse=True)
        return Response(pdf_files, status=status.HTTP_200_OK)

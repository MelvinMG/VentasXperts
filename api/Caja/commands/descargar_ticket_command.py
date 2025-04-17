# commands/descargar_ticket_command.py
from rest_framework import status
from rest_framework.response import Response
from django.http import FileResponse
import os
from .ticket_command import TicketCommand

class DescargarTicketCommand(TicketCommand):
    def execute(self, request, *args, **kwargs):
        pk = kwargs.get('pk')
        if not pk.endswith('.pdf'):
            pk += '.pdf'

        file_path = os.path.abspath(os.path.join('Caja', 'media', 'pdf_ticket', pk))
        if not os.path.exists(file_path):
            return Response({'message': 'Archivo no encontrado'}, status=status.HTTP_404_NOT_FOUND)

        return FileResponse(open(file_path, 'rb'), content_type='application/pdf')

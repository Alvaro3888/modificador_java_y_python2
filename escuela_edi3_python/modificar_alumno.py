# Importamos las librerías necesarias
import tkinter as tk                      # Tkinter: para crear la interfaz gráfica
from tkinter import messagebox            # messagebox: para mostrar mensajes emergentes
import mysql.connector                    # mysql.connector: para conectarse a la base de datos MySQL

# Función que se ejecuta al presionar el botón "Modificar"
def modificar_alumno():
    # Obtenemos los valores ingresados por el usuario en los campos de texto
    id_alumno = entry_id.get().strip()        # ID del alumno a modificar
    nombre = entry_nombre.get().strip()       # Nuevo nombre
    apellido = entry_apellido.get().strip()   # Nuevo apellido
    edad = entry_edad.get().strip()           # Nueva edad

    # Validamos que ningún campo esté vacío
    if not id_alumno or not nombre or not apellido or not edad:
        messagebox.showwarning("Campos vacíos", "Todos los campos son obligatorios.")
        return  # Salimos de la función si hay campos vacíos

    try:
        edad_int = int(edad)  # Convertimos la edad a entero (validación)

        # Conectamos con la base de datos MySQL
        conn = mysql.connector.connect(
            host="localhost",         # Servidor local (Laragon)
            user="root",              # Usuario por defecto
            password="",              # Contraseña (vacía si no configuraste una)
            database="escuela"        # Nombre de la base de datos
        )

        cursor = conn.cursor()  # Creamos el cursor para ejecutar comandos SQL

        # Consulta SQL para modificar el alumno con el ID ingresado
        sql = "UPDATE alumno SET nombre = %s, apellido = %s, edad = %s WHERE id = %s"
        cursor.execute(sql, (nombre, apellido, edad_int, id_alumno))  # Ejecutamos la consulta con los valores

        conn.commit()  # Confirmamos los cambios en la base de datos

        # Verificamos si se modificó alguna fila
        if cursor.rowcount > 0:
            messagebox.showinfo("Éxito", "Alumno modificado correctamente.")
        else:
            messagebox.showwarning("Sin cambios", "No se encontró un alumno con ese ID.")

        conn.close()  # Cerramos la conexión con la base de datos

    except ValueError:
        # Si edad o ID no son números válidos, mostramos un error
        messagebox.showerror("Error", "Edad e ID deben ser números.")

    except mysql.connector.Error as err:
        # Si hay un error de conexión o SQL, lo mostramos
        messagebox.showerror("Error de conexión", f"Ocurrió un error: {err}")

# ---------------- INTERFAZ GRÁFICA ---------------- #

# Creamos la ventana principal
ventana = tk.Tk()
ventana.title("Modificar Alumno")        # Título de la ventana
ventana.geometry("300x250")              # Tamaño de la ventana

# Campo para ingresar el ID del alumno
tk.Label(ventana, text="ID del alumno:").pack()
entry_id = tk.Entry(ventana)
entry_id.pack()

# Campo para ingresar el nuevo nombre
tk.Label(ventana, text="Nombre:").pack()
entry_nombre = tk.Entry(ventana)
entry_nombre.pack()

# Campo para ingresar el nuevo apellido
tk.Label(ventana, text="Apellido:").pack()
entry_apellido = tk.Entry(ventana)
entry_apellido.pack()

# Campo para ingresar la nueva edad
tk.Label(ventana, text="Edad:").pack()
entry_edad = tk.Entry(ventana)
entry_edad.pack()

# Botón que ejecuta la función modificar_alumno
tk.Button(ventana, text="Modificar", command=modificar_alumno).pack(pady=10)

# Iniciamos el bucle principal de la interfaz
ventana.mainloop()
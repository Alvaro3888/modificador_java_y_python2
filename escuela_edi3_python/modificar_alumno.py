import tkinter as tk
from tkinter import messagebox
import mysql.connector

def modificar_alumno():
    id_alumno = entry_id.get().strip()
    nombre = entry_nombre.get().strip()
    apellido = entry_apellido.get().strip()
    edad = entry_edad.get().strip()

    if not id_alumno or not nombre or not apellido or not edad:
        messagebox.showwarning("Campos vacíos", "Todos los campos son obligatorios.")
        return

    try:
        edad_int = int(edad)
        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="",  # Cambiá si tenés contraseña en Laragon
            database="escuela"
        )
        cursor = conn.cursor()
        sql = "UPDATE alumno SET nombre = %s, apellido = %s, edad = %s WHERE id = %s"
        cursor.execute(sql, (nombre, apellido, edad_int, id_alumno))
        conn.commit()

        if cursor.rowcount > 0:
            messagebox.showinfo("Éxito", "Alumno modificado correctamente.")
        else:
            messagebox.showwarning("Sin cambios", "No se encontró un alumno con ese ID.")

        conn.close()
    except ValueError:
        messagebox.showerror("Error", "Edad e ID deben ser números.")
    except mysql.connector.Error as err:
        messagebox.showerror("Error de conexión", f"Ocurrió un error: {err}")

# Interfaz gráfica
ventana = tk.Tk()
ventana.title("Modificar Alumno")
ventana.geometry("30x250")

tk.Label(ventana, text="ID del alumno:").pack()
entry_id = tk.Entry(ventana)
entry_id.pack()

tk.Label(ventana, text="Nombre:").pack()
entry_nombre = tk.Entry(ventana)
entry_nombre.pack()

tk.Label(ventana, text="Apellido:").pack()
entry_apellido = tk.Entry(ventana)
entry_apellido.pack()

tk.Label(ventana, text="Edad:").pack()
entry_edad = tk.Entry(ventana)
entry_edad.pack()

tk.Button(ventana, text="Modificar", command=modificar_alumno).pack(pady=10)

ventana.mainloop()
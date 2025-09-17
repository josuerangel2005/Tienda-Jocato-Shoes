#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import asyncio, os, sys, time, socket, signal, platform, subprocess
from typing import Dict, List

# ========= CONFIG (tus rutas) =========
SERVICES: List[Dict] = [
    {
        "name": "angular",
        "cwd": r"C:\Users\josue\OneDrive\Documentos\Cursos\Productos\Tienda\Angular\tienda-app",
        "cmd": "npm start -- --host 0.0.0.0 --port 4200",
        "host": "127.0.0.1",
        "port": 4200,
        "env": {}
    },
    {
        "name": "email",
        "cwd": r"C:\Users\josue\OneDrive\Documentos\Cursos\Productos\Tienda\Spring Boot\emailService\demo",
        # Si no tienes wrapper, cambia a: "mvn -q spring-boot:run"
        "cmd": "mvnw.cmd -q spring-boot:run",
        "host": "127.0.0.1",
        "port": 4042,
        "env": {"SPRING_PROFILES_ACTIVE": "dev", "SERVER_PORT": "4042"}
    },
    {
        "name": "jwt",
        "cwd": r"C:\Users\josue\OneDrive\Documentos\Cursos\Productos\Tienda\Spring Boot\jwtService\demo",
        "cmd": "mvnw.cmd -q spring-boot:run",
        "host": "127.0.0.1",
        "port": 8082,
        "env": {"SPRING_PROFILES_ACTIVE": "dev", "SERVER_PORT": "8082"}
    },
    {
        "name": "productos",
        "cwd": r"C:\Users\josue\OneDrive\Documentos\Cursos\Productos\Tienda\Spring Boot\productosService\productos",
        "cmd": "mvnw.cmd -q spring-boot:run",
        "host": "127.0.0.1",
        "port": 8081,
        "env": {"SPRING_PROFILES_ACTIVE": "dev", "SERVER_PORT": "8081"}
    },
    {
        "name": "stripe",
        "cwd": r"C:\Users\josue\OneDrive\Documentos\Cursos\Productos\Tienda\Spring Boot\stripe",
        "cmd": "mvnw.cmd -q spring-boot:run",
        "host": "127.0.0.1",
        "port": 9090,
        "env": {"SPRING_PROFILES_ACTIVE": "dev", "SERVER_PORT": "9090"}
    },
    {
        "name": "uploadimg",
        "cwd": r"C:\Users\josue\OneDrive\Documentos\Cursos\Productos\Tienda\Spring Boot\uploadImgService\imagenes",
        "cmd": "mvnw.cmd -q spring-boot:run",
        "host": "127.0.0.1",
        "port": 8080,
        "env": {"SPRING_PROFILES_ACTIVE": "dev", "SERVER_PORT": "8080"}
    },
]

# ========= Motor (no tocar) =========
IS_WINDOWS = platform.system() == "Windows"

def color(s: str, code: int) -> str:
    if IS_WINDOWS and os.getenv("NO_COLOR"):
        return s
    return f"\033[{code}m{s}\033[0m"

PALETTE = [36,32,33,35,34,31,96,92,93,95]
NAME_COLOR: Dict[str,int] = {}

def pick_color(name: str) -> int:
    if name not in NAME_COLOR:
        NAME_COLOR[name] = PALETTE[len(NAME_COLOR) % len(PALETTE)]
    return NAME_COLOR[name]

async def stream_output(name: str, proc: asyncio.subprocess.Process):
    prefix = f"[{name}]"
    c = pick_color(name)
    while True:
        line = await proc.stdout.readline()
        if not line:
            break
        print(color(prefix, c), line.decode(errors="ignore").rstrip(), flush=True)

def port_open(host: str, port: int, timeout_s: float = 0.8) -> bool:
    try:
        with socket.create_connection((host, port), timeout=timeout_s):
            return True
    except OSError:
        return False

async def wait_port_ready(name: str, host: str, port: int, deadline: float = 180.0):
    if not port:
        return
    start = time.time()
    warned = False
    while time.time() - start < deadline:
        if port_open(host, port):
            print(color(f"[{name}] Ready on {host}:{port}", 92), flush=True)
            return
        if not warned:
            print(color(f"[{name}] Waiting {host}:{port} ...", 90), flush=True)
            warned = True
        await asyncio.sleep(0.6)
    print(color(f"[{name}] ⚠ No abrió el puerto {port} en {int(deadline)}s", 91), flush=True)

async def run_service(s: Dict):
    env = os.environ.copy()
    env.update(s.get("env", {}))
    kwargs = {
        "cwd": s.get("cwd"),
        "env": env,
        "stdout": asyncio.subprocess.PIPE,
        "stderr": asyncio.subprocess.STDOUT,
    }
    if IS_WINDOWS:
        kwargs["creationflags"] = subprocess.CREATE_NEW_PROCESS_GROUP  # type: ignore
    else:
        kwargs["start_new_session"] = True  # type: ignore

    proc = await asyncio.create_subprocess_shell(s["cmd"], **kwargs)
    s["_proc"] = proc
    asyncio.create_task(stream_output(s["name"], proc))
    asyncio.create_task(wait_port_ready(s["name"], s.get("host","127.0.0.1"), s.get("port")))
    return await proc.wait()

async def terminate_service(s: Dict, force_after: float = 5.0):
    proc: asyncio.subprocess.Process = s.get("_proc")
    if not proc or proc.returncode is not None:
        return
    name = s["name"]
    try:
        if IS_WINDOWS:
            try:
                proc.send_signal(signal.CTRL_BREAK_EVENT)  # type: ignore
            except Exception:
                proc.terminate()
            try:
                await asyncio.wait_for(proc.wait(), timeout=force_after)
            except asyncio.TimeoutError:
                subprocess.run(["taskkill", "/PID", str(proc.pid), "/T", "/F"],
                               stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        else:
            os.killpg(os.getpgid(proc.pid), signal.SIGTERM)  # type: ignore
            try:
                await asyncio.wait_for(proc.wait(), timeout=force_after)
            except asyncio.TimeoutError:
                os.killpg(os.getpgid(proc.pid), signal.SIGKILL)  # type: ignore
        print(color(f"[{name}] detenido", 90))
    except ProcessLookupError:
        pass

async def orchestrate(selected: List[str]):
    targets = [s for s in SERVICES if s["name"] in selected]
    if not targets:
        print("No hay servicios para lanzar.")
        return 1
    tasks = [asyncio.create_task(run_service(s)) for s in targets]
    print(color("▶ Lanzando: " + ", ".join([s['name'] for s in targets]), 96), flush=True)
    try:
        done, _ = await asyncio.wait(tasks, return_when=asyncio.FIRST_EXCEPTION)
        for d in done:
            rc = d.result()
            if rc not in (0, None):
                print(color(f"✖ Un servicio terminó con código {rc}. Cerrando...", 91), flush=True)
                break
    except KeyboardInterrupt:
        print(color("\n⏹ Interrupción. Cerrando servicios...", 93), flush=True)
    finally:
        await asyncio.gather(*(terminate_service(s) for s in targets), return_exceptions=True)
    return 0

def parse_args() -> List[str]:
    if len(sys.argv) == 1:
        return [s["name"] for s in SERVICES]
    wanted = set(sys.argv[1:])
    known = {s["name"] for s in SERVICES}
    unknown = [w for w in wanted if w not in known]
    if unknown:
        print("Servicios desconocidos:", ", ".join(unknown))
        print("Disponibles:", ", ".join(sorted(known)))
        sys.exit(2)
    return list(wanted)

if __name__ == "__main__":
    selected = parse_args()
    for s in SERVICES:
        if not os.path.isdir(s["cwd"]):
            print(color(f"⚠ Directorio no existe: {s['cwd']} ({s['name']})", 91))
    try:
        code = asyncio.run(orchestrate(selected))
    except KeyboardInterrupt:
        code = 1
    sys.exit(code)

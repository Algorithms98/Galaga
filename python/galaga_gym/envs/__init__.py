from gym.envs.registration import register

register(id="GalagaEnv-v0",
  entry_point="envs.galaga_env:GalagaEnv"
)
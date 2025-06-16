package wook.pool.board.screen.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import wook.pool.board.R
import wook.pool.board.data.enums.Handicap
import wook.pool.board.data.model.Player
import wook.pool.board.databinding.FragmentPlayersBinding
import wook.pool.board.global.base.BaseFragment
import wook.pool.board.global.event.EventObserver
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.dialog.AddPlayerDialog
import wook.pool.board.screen.scoreboard.ScoreBoardViewModel

class PlayersFragment(override val layoutResId: Int = R.layout.fragment_players) :
        BaseFragment<FragmentPlayersBinding>(),
        View.OnClickListener {

    private val args: PlayersFragmentArgs by navArgs()
    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()
    private val playerAdapter: PlayerAdapter by lazy {
        PlayerAdapter(
            onClickPlayer = {
                playersViewModel.setPlayer(it, args.isModeChoiceLeft)
            },
            onLongClickPlayer = { player ->
                showDeletePlayerDialog(player)
            },
            onClickAddPlayer = {
                showAddPlayerDialog()
            }
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState).apply {
        binding.apply {
            selectedHandicapIndex = Handicap.HANDICAP_5.ordinal
            onClickHandicapButton = this@PlayersFragment.onClickHandicapButton
            listener = this@PlayersFragment
            recyclerPlayers.adapter = playerAdapter
            layoutPlayers.setOnRefreshListener { playersViewModel.getPlayers() }
        }
        initObserver()
    }

    private fun initObserver() {
        with(playersViewModel) {
            players.observe(viewLifecycleOwner) {
                selectHandicap(selectedHandicap.value ?: Handicap.HANDICAP_5)
            }
            selectedHandicap.observe(viewLifecycleOwner) {
                submitPlayers(it.value)
                binding.selectedHandicapIndex = it.ordinal
                binding.layoutPlayers.isRefreshing = false
            }
            isPlayerSetSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    playersViewModel.getHeadToHeadRecords()
                    scoreBoardViewModel.setNavDirection(PlayersFragmentDirections.actionFragmentPlayerListToFragmentSetting())
                } else {
                    showDialogDuplicatedPlayer()
                }
            })
            deletePlayerEvent.observe(viewLifecycleOwner, EventObserver { result ->
                val (success, playerName) = result
                if (success) {
                    showDeleteSuccessDialog(playerName)
                } else {
                    showDeleteFailedDialog()
                }
            })
            addPlayerEvent.observe(viewLifecycleOwner, EventObserver { result ->
                val (success, message) = result
                if (success) {
                    showAddSuccessDialog(message)
                } else {
                    showAddFailedDialog(message)
                }
            })
        }
    }

    private fun submitPlayers(handicap: Int) {
        playersViewModel.players.value?.let {
            playerAdapter.submitList(it[handicap])
        }
    }

    private fun showDialogDuplicatedPlayer() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK)
                    .setTitle(getString(R.string.fragment_player_list_duplicated_player_title))
                    .setMessage(getString(R.string.fragment_player_list_duplicated_player_desc))
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }

    private fun showDeletePlayerDialog(player: Player) {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK_CANCEL)
                    .setTitle("회원 삭제")
                    .setMessage("${player.name}님을 삭제하시겠습니까?")
                    .setLeftButtonText(getString(R.string.common_cancel))
                    .setRightButtonText("삭제")
                    .setOnClickLeft { dialog ->
                        dialog.dismiss()
                    }
                    .setOnClickRight { dialog ->
                        playersViewModel.deletePlayer(player)
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }

    private fun showDeleteSuccessDialog(playerName: String?) {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK)
                    .setTitle("삭제 완료")
                    .setMessage("${playerName ?: "회원"}님을 삭제하였습니다.")
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }

    private fun showDeleteFailedDialog() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK)
                    .setTitle("삭제 실패")
                    .setMessage("회원 삭제에 실패했습니다.\n잠시 후 다시 시도해주세요.")
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }
    private fun showAddPlayerDialog() {
        hostActivityContext?.let { context ->
            AddPlayerDialog.Builder()
                    .setOnClickCancel { dialog ->
                        dialog.dismiss()
                    }
                    .setOnClickConfirm { dialog, playerName ->
                        when {
                            playerName.isBlank() -> {
                                Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                playersViewModel.addPlayer(playerName.trim())
                                dialog.dismiss()
                            }
                        }
                    }
                    .create(context)
                    .show()
        }
    }
    private fun showAddSuccessDialog(playerName: String?) {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK)
                    .setTitle("추가 완료")
                    .setMessage("${playerName ?: "회원"}님이 추가되었습니다.")
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }

    private fun showAddFailedDialog(message: String?) {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK)
                    .setTitle("추가 실패")
                    .setMessage(message ?: "회원 추가에 실패했습니다.")
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }


    private val onClickHandicapButton = View.OnClickListener {
        with(binding) {
            val index = binding.layoutHandicapSelector.indexOfChild(it)
            if (selectedHandicapIndex == index) return@OnClickListener
            Handicap.values().firstOrNull { it.value == index + 3 }?.let { selectedHandicapIndex ->
                playersViewModel.selectHandicap(selectedHandicapIndex)
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                imgBtnBack ->
                    scoreBoardViewModel.setNavDirection(PlayersFragmentDirections.actionFragmentPlayerListToFragmentSetting())
                textBtnMinusHandicap -> {
                    playersViewModel.selectedHandicap.value?.ordinal?.minus(1)?.let {
                        kotlin.runCatching {
                            playersViewModel.selectHandicap(Handicap.values()[it])
                        }
                    }
                }
                textBtnPlusHandicap -> {
                    playersViewModel.selectedHandicap.value?.ordinal?.plus(1)?.let {
                        kotlin.runCatching {
                            playersViewModel.selectHandicap(Handicap.values()[it])
                        }
                    }
                }
                else -> return
            }
        }
    }
}